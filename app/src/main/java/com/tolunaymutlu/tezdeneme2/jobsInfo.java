/*package com.tolunaymutlu.tezdeneme2;

import java.net.URI;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.LogicalOperator;
import microsoft.exchange.webservices.data.core.enumeration.search.SortDirection;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;

public class jobsInfo {

    User usr = new User();
    String msg;
    String clearJobs;
    ArrayList<String> arrayList = new ArrayList<>();
    public void getJob() {
        ExchangeService exchangeService = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
        ExchangeCredentials credentials = new WebCredentials("tez2hedef@outlook.com", "tolunay123");
        exchangeService.setCredentials(credentials);

        Thread thread21 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    usr.getUserThings();
                    //thread sleep ile gecikme yuzunden olusan ekrana yazdirmamayi onluyoruz.
                    Thread.sleep(700);
                    exchangeService.setUrl(new URI("https://outlook.office365.com/EWS/Exchange.asmx"));
                    ItemView view = new ItemView(10);
                    view.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Descending);
                    view.setPropertySet(new PropertySet(BasePropertySet.IdOnly,ItemSchema.Subject,ItemSchema.DateTimeReceived));
                    FindItemsResults<Item> findResult =
                            exchangeService.findItems(WellKnownFolderName.Inbox,
                                    new SearchFilter.SearchFilterCollection(
                                            LogicalOperator.Or, new SearchFilter.ContainsSubstring(ItemSchema.Subject,usr.getAttiribute()),
                                            new SearchFilter.ContainsSubstring(ItemSchema.Subject,"CSS"),new SearchFilter.ContainsSubstring(ItemSchema.Subject, usr.getAttiribute2())),view);
                    exchangeService.loadPropertiesForItems(findResult, PropertySet.FirstClassProperties);


                    for (Item item :findResult){
                        msg = item.getBody().toString();
                        String patternString = "(.*<body.*?>)(.*)(</body>.*)";
                        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
                        String htmlStr = msg;
                        Matcher matcher = pattern.matcher(htmlStr);
                        String htmlContent = matcher.matches() ? matcher.group(2) : "";
                        // html content
                        // System.out.println(htmlContent);
                        // stripped html content
                        clearJobs = htmlContent.replaceAll("<.*?>", "");
                       // System.out.println(clearJobs);
                        arrayList.add(clearJobs);
                        System.out.println(arrayList.get(arrayList.size()-1));
                        JobsActivity a = new JobsActivity();
                    }



                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread21.start();
    }
}
*/