package de.thro.inf.prg3.a10.workers;

import de.thro.inf.prg3.a10.internals.displaying.ProgressReporter;
import de.thro.inf.prg3.a10.kitchen.KitchenHatch;
import de.thro.inf.prg3.a10.model.Dish;
import de.thro.inf.prg3.a10.model.Order;

public class Cook implements Runnable
{
	private String           name;
	private ProgressReporter progressReporter;
	private KitchenHatch     kitchenHatch;

	public Cook(String name, ProgressReporter progressReporter, KitchenHatch kitchenHatch)
	{
		this.name             = name;
		this.progressReporter = progressReporter;
		this.kitchenHatch     = kitchenHatch;
	}

	@Override
	public void run()
	{
		while (this.kitchenHatch.getOrderCount() > 0)
		{
			Order order = this.kitchenHatch.dequeueOrder();
			Dish  dish  = new Dish(order.getMealName());

			try
			{
				Thread.sleep(dish.getCookingTime());
			}

			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			this.kitchenHatch.enqueueDish(dish);
			this.progressReporter.updateProgress();
		}

		this.progressReporter.notifyCookLeaving();
	}
}
