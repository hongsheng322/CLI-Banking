import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.io.IOException;
import java.net.*;
import java.net.http.*;

public class CurrencyConverter {
    URL url;

    /**
    * Check server time from CSV and update limit and compound interest respectively
    * @param moneyToConvert the amount to conver
    * @param fromCurrency the currency to convert from
    * @param toCurrency the currency to convert to
    * @throws InterruptedException 
    * @throws IOException 
     */
    public double convertCurrency(double moneyToConvert, String fromCurrency, String toCurrency) {
        try {
            URL url = new URL("https://currency-converter5.p.rapidapi.com/currency/convert?format=json&from="+fromCurrency+
                    "&to="+toCurrency+"&amount="+moneyToConvert);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://currency-converter5.p.rapidapi.com/currency/convert?format=json&from="+fromCurrency+
                        "&to="+toCurrency+"&amount="+moneyToConvert))
                .header("X-RapidAPI-Host", "currency-converter5.p.rapidapi.com")
                .header("X-RapidAPI-Key", "286496e279msh17c9c2f03417bd5p10ab67jsncdeb1a575945")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //int statusCode = response.statusCode();
        String body = response.body();
        Object obj = JSONValue.parse(body);
        JSONObject jsonObj = (JSONObject) obj;
        JSONObject jsonObj2 = (JSONObject)jsonObj.get("rates");
        JSONObject jsonObj3 = (JSONObject)jsonObj2.get("SGD");
        double amount = Double.parseDouble((String)jsonObj3.get("rate_for_amount"));
        return amount;
    }
}
