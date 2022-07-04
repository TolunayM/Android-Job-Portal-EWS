package com.tolunaymutlu.tezdeneme2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import com.tolunaymutlu.tezdeneme2.databinding.ActivityJobsBinding;

import org.w3c.dom.Text;

import java.net.URI;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.LogicalOperator;
import microsoft.exchange.webservices.data.core.enumeration.search.SortDirection;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;

public class JobsActivity extends AppCompatActivity {

     public ActivityJobsBinding binding;
     ArrayList<String> arrayList = new ArrayList<>();
     ArrayList<String> senderList = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJobsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        User usr = new User();


        ExchangeService serviceExc = new ExchangeService(ExchangeVersion.Exchange2010);
        ExchangeCredentials credentials = new WebCredentials("userMail","password");
        serviceExc.setCredentials(credentials);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Finding Jobs");
        progressDialog.setMessage("Thanks for your patience");
        progressDialog.setCanceledOnTouchOutside(false);


        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JobsActivity.this,ProfileActivity.class));
            }
        });

        /*jobsInfo bb = new jobsInfo();
        bb.getJob();*/
        //We can send a mail when button click
        /*binding.jobsMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            serviceExc.setUrl(new URI("https://outlook.office365.com/EWS/Exchange.asmx"));
                            EmailMessage message = new EmailMessage(serviceExc);
                            message.getToRecipients().add("tez2hedef@outlook.com");
                            message.setSubject("I did it!");
                            message.setBody(MessageBody.getMessageBodyFromText("This message was sent from EWS"));
                            message.send();
                            System.out.println(message.getBody());


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();


            }
        });*/

        binding.feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedbackMail = "adminMail";
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + feedbackMail));
                intent.putExtra(Intent.EXTRA_EMAIL,feedbackMail);
                //intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent,"You can send feedback to us"));
            }
        });

        binding.jobsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                Thread thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            usr.getUserThings();
                            //There is a delay if im not using thread.sleep() and this cause a bug

                            serviceExc.setUrl(new URI("https://outlook.office365.com/EWS/Exchange.asmx"));
                            ItemView view = new ItemView(10);
                            view.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Descending);
                            view.setPropertySet(new PropertySet(BasePropertySet.IdOnly,ItemSchema.Subject,ItemSchema.DateTimeReceived));
                            Thread.sleep(2000);
                            progressDialog.dismiss();
                            FindItemsResults<Item> findResult =
                                    serviceExc.findItems(WellKnownFolderName.Inbox,
                                            new SearchFilter.SearchFilterCollection(
                                                    LogicalOperator.Or, new SearchFilter.ContainsSubstring(ItemSchema.Subject,usr.getAttiribute()),
                                                    new SearchFilter.ContainsSubstring(ItemSchema.Subject,usr.getAttiribute3()),new SearchFilter.ContainsSubstring(ItemSchema.Subject, usr.getAttiribute2())),view);
                            serviceExc.loadPropertiesForItems(findResult, PropertySet.FirstClassProperties);
                            System.out.println("Total Job Count " + findResult.getTotalCount());

                            for (Item item :findResult){
                                System.out.println(item.getSubject());

                                //we separate html tags with mail
                                String msg = item.getBody().toString();
                                String patternString = "(.*<body.*?>)(.*)(</body>.*)";
                                Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
                                String htmlStr = msg;
                                Matcher matcher = pattern.matcher(htmlStr);
                                String htmlContent = matcher.matches() ? matcher.group(2) : "";

                                String clearJobs = htmlContent.replaceAll("<.*?>", "");
                                arrayList.add(clearJobs);

                                //getting sender mail
                                String senderMail = ((EmailMessage) item).getSender().getAddress();
                                senderList.add(senderMail);


                                System.out.println(clearJobs);
                                if(arrayList.size() == 1){
                                    binding.jobView1.setText(arrayList.get(0));
                                }
                                if(arrayList.size() == 2){
                                    binding.jobView2.setText(arrayList.get(1));
                                }
                                if(arrayList.size() == 3){
                                    binding.jobView3.setText(arrayList.get(2));
                                }
                                if(arrayList.size() == 4){
                                    binding.jobView4.setText(arrayList.get(3));
                                }
                                if(arrayList.size() == 5){
                                    binding.jobView5.setText(arrayList.get(4));
                                }
                                if(arrayList.size() == 6){
                                    binding.jobView6.setText(arrayList.get(5));
                                }





                               // System.out.println(htmlContent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread2.start();
            }
        });
            //textview
        binding.jobView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                jobAlert();



                return false;
            }
        });
    }

    public void jobAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(JobsActivity.this);
        alert.setTitle("Apply");
        alert.setMessage(arrayList.get(0));
        alert.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendMail();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),senderList.get(0),Toast.LENGTH_LONG).show();
            }
        });
        alert.show();
    }

    public void sendMail(){
        String recipient = senderList.get(0);
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + recipient));
        intent.putExtra(Intent.EXTRA_EMAIL,recipient);
        //intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Select a mail app on your phone"));
    }




    @Override
    protected void onStop() {
        super.onStop();
        arrayList.clear();
    }
}