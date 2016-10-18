package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.sample.stockwatcher.shared.FieldVerifier;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

public class StockWatcher implements EntryPoint, ExceptionListener {
	private FlexTable stocksFlexTable = new FlexTable();
	private SuggestBox newCurrencySuggestBox = new SuggestBox();
	private Label lastUpdatedLabel = new Label();
	private List<String> stocks = new ArrayList<String>();
	private StockPriceServiceAsync stockPriceSvc = GWT.create(StockPriceService.class);
	private StockWatcherConstants constants = GWT.create(StockWatcherConstants.class);
	private StockWatcherMessages messages = GWT.create(StockWatcherMessages.class);
	private Label errorMsgLabel = new Label();
	private DateBox dateBox = new DateBox();
	private ExceptionHandler exceptionHandler = new ExceptionHandler(messages, this);

	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {
		Window.setTitle(constants.stockWatcher());
		RootPanel.get("appTitle").add(new Label(constants.stockWatcher()));

		// Create table for stock data.
		stocksFlexTable.setText(0, 0, constants.symbol());
		stocksFlexTable.setText(0, 1, constants.price());
		stocksFlexTable.setText(0, 2, constants.change());
		stocksFlexTable.setText(0, 3, constants.remove());

		// Add styles to elements in the stock list table.
		stocksFlexTable.setCellPadding(6);
		stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
		stocksFlexTable.addStyleName("watchList");
		stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
		stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
		stocksFlexTable.getCellFormatter().addStyleName(0, 3, "watchListRemoveColumn");

		// Create criteria panel
		HorizontalPanel criteriaPanel = new HorizontalPanel();

		dateBox.setFormat(new DateBox.DefaultFormat(DateUtils.DATE_FORMAT));
		dateBox.setValue(new Date(), true);
		criteriaPanel.add(dateBox);

		final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		stockPriceSvc.getCurrencyCodes(new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				exceptionHandler.onException(caught);
			}

			@Override
			public void onSuccess(List<String> result) {
				oracle.addAll(result);
			}
		});
		newCurrencySuggestBox = new SuggestBox(oracle);
		newCurrencySuggestBox.getElement().setPropertyString("placeholder", constants.add());
		newCurrencySuggestBox.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);

		criteriaPanel.add(newCurrencySuggestBox);
		criteriaPanel.getElement().getStyle().setPaddingBottom(35, Style.Unit.PX);

		// Create main panel.
		errorMsgLabel.setStyleName("errorMessage");
		errorMsgLabel.setVisible(false);

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(criteriaPanel);
		mainPanel.add(errorMsgLabel);
		mainPanel.add(stocksFlexTable);
		mainPanel.add(lastUpdatedLabel);

		// Associate the Main panel with the HTML host page.
		RootPanel.get("stockList").add(mainPanel);

		// Setup action bindings
		newCurrencySuggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
				addStock(event.getSelectedItem().getReplacementString());
			}
		});

		dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				refreshWatchList();
			}
		});
	}

	private void addStock(final String symbol) {
		if(!FieldVerifier.isValidName(symbol)){
			exceptionThrown(messages.invalidInput());
		}

		newCurrencySuggestBox.setFocus(true);
		newCurrencySuggestBox.setText("");

		if (stocks.contains(symbol))
			return;

		int row = stocksFlexTable.getRowCount();
		stocks.add(symbol);
		stocksFlexTable.setText(row, 0, symbol);
		stocksFlexTable.setWidget(row, 2, new Label());
		stocksFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
		stocksFlexTable.getCellFormatter().addStyleName(row, 2, "watchListNumericColumn");
		stocksFlexTable.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");

		Button removeStockButton = new Button("x");
		removeStockButton.addStyleDependentName("remove");
		removeStockButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int removedIndex = stocks.indexOf(symbol);
				stocks.remove(removedIndex);
				stocksFlexTable.removeRow(removedIndex + 1);
				refreshWatchList();
			}
		});
		stocksFlexTable.setWidget(row, 3, removeStockButton);

		refreshWatchList();
	}

	private void refreshWatchList() {
		if (stocks.isEmpty()) {
			return;
		}

		Date filterDate = dateBox.getValue();

		// Set up the callback object.
		AsyncCallback<List<StockPrice>> callback = new AsyncCallback<List<StockPrice>>() {
			public void onFailure(Throwable caught) {
				exceptionHandler.onException(caught);
			}

			public void onSuccess(List<StockPrice> result) {
				updateTable(result);
			}
		};

		// Make the call to the stock price service.
		stockPriceSvc.getPrices(stocks, filterDate, callback);
	}

	private void updateTable(List<StockPrice> prices) {
		for (StockPrice stockPrice : prices) {
			updateTable(stockPrice);
		}

		// Display timestamp showing last refresh.
		lastUpdatedLabel.setText(messages.lastUpdate(new Date()));

		// Clear any errors.
		errorMsgLabel.setVisible(false);
	}

	private void updateTable(StockPrice price) {
		// Make sure the stock is still in the stock table.
		if (!stocks.contains(price.getSymbol())) {
			return;
		}

		int row = stocks.indexOf(price.getSymbol()) + 1;

		// Format the data in the Price and Change fields.
		String priceText = NumberFormat.getFormat("#,##0.00000").format(price.getPrice());
		NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
		String currentPriceText = NumberFormat.getFormat("#,##0.00000").format(price.getCurrentPrice());
		String changePercentText = changeFormat.format(price.getChangePercent());

		// Populate the Price and Change fields with new data.
		stocksFlexTable.setText(row, 1, priceText);
		Label changeWidget = (Label) stocksFlexTable.getWidget(row, 2);
		changeWidget.setText(currentPriceText + " (" + changePercentText + "%)");

		// Change the color of text in the Change field based on its value.
		String changeStyleName = "noChange";
		if (price.getPrice().compareTo(price.getCurrentPrice()) > 0) {
			changeStyleName = "negativeChange";
		} else if (price.getPrice().compareTo(price.getCurrentPrice()) < 0) {
			changeStyleName = "positiveChange";
		}

		changeWidget.setStyleName(changeStyleName);
	}

	@Override
	public void exceptionThrown(String msg) {
		errorMsgLabel.setText(msg);
		errorMsgLabel.setVisible(true);

		for (int row = 1; row <= stocks.size(); row++) {
			// Format the data in the Price and Change fields.
			String noData = "-";

			// Populate the Price and Change fields with new data.
			stocksFlexTable.setText(row, 1, noData);
			Label changeWidget = (Label) stocksFlexTable.getWidget(row, 2);
			changeWidget.setText(noData);

			// Change the color of text in the Change field based on its value.
			String changeStyleName = "noChange";

			changeWidget.setStyleName(changeStyleName);
		}
	}
}
