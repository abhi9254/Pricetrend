package main;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PriceUpdater {

	public PriceUpdater() {

	}

	public void updatePrices() {
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);
		System.out.println("Current time " + LocalDateTime.now().getHour() + " hr");

		ArrayList<Integer> p_list = new ArrayList<Integer>();
		Products prds = new Products();

		p_list = prds.getAllProducts();
		for (int p_id : p_list) {

			// check time of day
			if (LocalDateTime.now().getHour() > 5 && LocalDateTime.now().getHour() < 8) {
				System.out.println("Downtime");
				scheduledExecutorService.shutdown();
				break;
			}

			else {
				ArrayList<URL> p_urls = new ArrayList<URL>();
				Product prd = new Product(p_id);
				p_urls = prd.getProductURLs();
				for (URL p_url : p_urls)
					scheduledExecutorService.scheduleAtFixedRate(new PriceUpdaterService(p_id, p_url), 0, 30,
							TimeUnit.MINUTES);
			}
		}

	}
}

class PriceUpdaterService implements Runnable {

	private int p_id;
	private URL p_url;

	PriceUpdaterService(int p_id, URL p_url) {
		this.p_id = p_id;
		this.p_url = p_url;
	}

	@Override
	public void run() {
		updateProductPrice(p_id, p_url);
	}

	private void updateProductPrice(int p_id, URL p_url) {
		try {
			Crawler crw = new Crawler();
			Float p_price = crw.getPriceFromURL(p_url);
			System.out.println(LocalDateTime.now() + " Updated p_id " + p_id + " : " + p_price);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
