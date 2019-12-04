package de.thro.inf.prg3.a10.workers;

import de.thro.inf.prg3.a10.internals.displaying.ProgressReporter;
import de.thro.inf.prg3.a10.kitchen.KitchenHatch;
import de.thro.inf.prg3.a10.model.Dish;
import de.thro.inf.prg3.a10.model.Order;

import java.util.Random;

public class Waiter implements Runnable
{
	private String           name;
	private ProgressReporter progressReporter;
	private KitchenHatch     kitchenHatch;

	public Waiter(String name, ProgressReporter progressReporter, KitchenHatch kitchenHatch)
	{
		this.name             = name;
		this.progressReporter = progressReporter;
		this.kitchenHatch     = kitchenHatch;
	}

	@Override
	public void run()
	{
		Dish dish;

		do
		{
			dish = this.kitchenHatch.dequeueDish(5000);

			if (dish != null)
			{
				try
				{
					Thread.sleep(new Random().nextInt(10000));
				}

				catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				this.progressReporter.updateProgress();
			}
		}
		while (this.kitchenHatch.getDishesCount() > 0 || dish == null);

		this.progressReporter.notifyWaiterLeaving();
	}
}
