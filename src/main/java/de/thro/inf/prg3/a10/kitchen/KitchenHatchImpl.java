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
		if (this.getOrderCount() == 0)
		{
			try
			{
				wait(timeout);
			}

			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		synchronized (this.orders)
		{
			Order order = this.orders.removeFirst();

			notify();

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
		if (this.getDishesCount() == 0)
		{
			try
			{
				wait(timeout);
			}

			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		synchronized (this.dishes)
		{
			Dish dish = this.dishes.removeFirst();

			notify();

			return dish;
		}
	}

	@Override
	public void enqueueDish(Dish m)
	{
		if (this.getDishesCount() == this.MAX_MEALS)
		{
			try
			{
				wait();
			}

			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		synchronized (this.dishes)
		{
			this.dishes.addLast(m);

			notify();
		}
	}

	@Override
	public int getDishesCount()
	{
		return this.dishes.size();
	}
}
