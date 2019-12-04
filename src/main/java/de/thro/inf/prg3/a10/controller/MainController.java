package de.thro.inf.prg3.a10.controller;

import de.thro.inf.prg3.a10.internals.displaying.ProgressReporter;
import de.thro.inf.prg3.a10.kitchen.KitchenHatch;
import de.thro.inf.prg3.a10.kitchen.KitchenHatchImpl;
import de.thro.inf.prg3.a10.model.Order;
import de.thro.inf.prg3.a10.util.NameGenerator;
import de.thro.inf.prg3.a10.workers.Cook;
import de.thro.inf.prg3.a10.workers.Waiter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;

import java.net.URL;
import java.util.Deque;
import java.util.LinkedList;
import java.util.ResourceBundle;

import static de.thro.inf.prg3.a10.KitchenHatchConstants.*;

public class MainController implements Initializable
{

	private final ProgressReporter progressReporter;
	private final KitchenHatch kitchenHatch;
	private final NameGenerator nameGenerator;

	@FXML
	private ProgressIndicator waitersBusyIndicator;

	@FXML
	private ProgressIndicator cooksBusyIndicator;

	@FXML
	private ProgressBar kitchenHatchProgress;

	@FXML
	private ProgressBar orderQueueProgress;

	public MainController()
	{
		this.nameGenerator = new NameGenerator();

		Deque<Order> orders = new LinkedList<>();

		for (int orderIndex = 0; orderIndex < ORDER_COUNT; orderIndex ++)
			orders.addLast(new Order(this.nameGenerator.getRandomDish()));

		this.kitchenHatch = new KitchenHatchImpl(KITCHEN_HATCH_SIZE, orders);
		this.progressReporter = new ProgressReporter(kitchenHatch, COOKS_COUNT, WAITERS_COUNT, ORDER_COUNT, KITCHEN_HATCH_SIZE);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		orderQueueProgress.progressProperty().bindBidirectional(this.progressReporter.orderQueueProgressProperty());
		kitchenHatchProgress.progressProperty().bindBidirectional(this.progressReporter.kitchenHatchProgressProperty());
		waitersBusyIndicator.progressProperty().bindBidirectional(this.progressReporter.waitersBusyProperty());
		cooksBusyIndicator.progressProperty().bind(this.progressReporter.cooksBusyProperty());

		for (int cookIndex = 0; cookIndex < COOKS_COUNT; cookIndex ++)
			new Thread(new Cook(this.nameGenerator.generateName(), this.progressReporter, this.kitchenHatch)).start();

		for (int waiterIndex = 0; waiterIndex < WAITERS_COUNT; waiterIndex ++)
			new Thread(new Waiter(this.nameGenerator.generateName(), this.progressReporter, this.kitchenHatch)).start();
	}
}
