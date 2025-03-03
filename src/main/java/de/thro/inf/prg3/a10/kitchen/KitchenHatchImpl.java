package de.thro.inf.prg3.a10.kitchen;

import de.thro.inf.prg3.a10.model.Dish;
import de.thro.inf.prg3.a10.model.Order;

import java.util.Deque;
import java.util.LinkedList;

public class KitchenHatchImpl implements KitchenHatch
{
	private final int MAX_MEALS;

	private final Deque<Order> orders;
	private final Deque<Dish>  dishes;

	public KitchenHatchImpl(int maxMeals, Deque<Order> orders)
	{
		this.MAX_MEALS = maxMeals;

		this.orders = orders;
		this.dishes = new LinkedList<>();
	}

	@Override
	public int getMaxDishes()
	{
		return this.MAX_MEALS;
	}

	@Override
	public Order dequeueOrder(long timeout)
	{
		synchronized (this.orders)
		{
			while (this.getOrderCount() == 0)
			{
				try
				{
					this.orders.wait(timeout);
				}

				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

			Order order = this.orders.removeFirst();

			this.orders.notifyAll();

			return order;
		}
	}

	@Override
	public int getOrderCount()
	{
		return this.orders.size();
	}

	@Override
	public Dish dequeueDish(long timeout)
	{
		long timeStamp = System.nanoTime();

		synchronized (this.dishes)
		{
			while (this.getDishesCount() == 0)
			{
				try
				{
					this.dishes.wait(timeout);
				}

				catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				if (timeout > 0 && dishes.size() == 0 && System.nanoTime() - timeStamp > timeout * 1000)
				{
					this.dishes.notifyAll();

					return null;
				}
			}

			Dish dish = this.dishes.removeFirst();
			System.out.println(dish.getMealName());

			this.dishes.notifyAll();

			return dish;
		}
	}

	@Override
	public void enqueueDish(Dish m)
	{
		synchronized (this.dishes)
		{
			while (this.getDishesCount() == this.MAX_MEALS)
			{
				try
				{
					this.dishes.wait();
				}

				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

			this.dishes.addLast(m);

			this.dishes.notifyAll();
		}
	}

	@Override
	public int getDishesCount()
	{
		return this.dishes.size();
	}
}
