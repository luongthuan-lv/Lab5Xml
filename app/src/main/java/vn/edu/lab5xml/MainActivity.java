package vn.edu.lab5xml;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<TinTuc> tinTucs;
    private EditText editText;
    ListView lvList;
    private DataAdapter dataAdapter;

    private DataAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tinTucs=new ArrayList<>();
        lvList=findViewById(R.id.lvList);
       dataAdapter=new DataAdapter(MainActivity.this,tinTucs);
        lvList.setAdapter(dataAdapter);
}

    public void getData(View view) {
       // String url=editText.getText().toString().trim();
        String url="https://vnexpress.net/rss/suc-khoe.rss";
        Data data=new Data();
        data.execute(url);
    }

    class Data extends AsyncTask<String,Long, ArrayList<TinTuc>>{


        @Override
        protected ArrayList<TinTuc> doInBackground(String... strings) {
            String link = strings[0];
            try {
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();
                //Khởi tạo đối tượng bằng XML
                XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();

                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();

                xmlPullParser.setInput(inputStream, "utf-8");


                int eventType = xmlPullParser.getEventType();
                TinTuc tinTuc = null;
                String text = "";
                while (eventType != xmlPullParser.END_DOCUMENT) {
                    eventType = xmlPullParser.getEventType();
                    String tag = xmlPullParser.getName();
                    switch (eventType) {
                        //Bắt đầu thẻ
                        case XmlPullParser.START_TAG:
                            if (tag.equalsIgnoreCase("item")) {
                                tinTuc = new TinTuc();
                            }
                            break;
                        case XmlPullParser.TEXT:
                            text = xmlPullParser.getText();
                            break;
                        case XmlPullParser.END_TAG:
                            if (tinTuc != null){


                                if (tag.equalsIgnoreCase("title")) {
                                    tinTuc.title = text;
                                } else  if (tag.equalsIgnoreCase("description")){
                                    tinTuc.description=text;
                                } else  if (tag.equalsIgnoreCase("pubDate")){
                                    tinTuc.pubDate=text;
                                } else  if (tag.equalsIgnoreCase("image")){
                                    tinTuc.image=text;
                                } else if (tag.equalsIgnoreCase("item")){
                                    tinTucs.add(tinTuc);
                                }
                            }
                            break;

                    }
                    xmlPullParser.next();
                }


            } catch (MalformedURLException e) {
                //url bị sai : url
                e.printStackTrace();
            } catch (IOException e) {
                //Không kết nối đc : openConnection
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                //Sai định dạng : newInstance
                e.printStackTrace();
            }

            return tinTucs;
        }

        @Override
        protected void onPostExecute( ArrayList<TinTuc> tinTucs) {
            super.onPostExecute(tinTucs);
            Toast.makeText(MainActivity.this,tinTucs.size()+"",Toast.LENGTH_SHORT).show();
            dataAdapter.notifyDataSetChanged();
        }
    }

}
