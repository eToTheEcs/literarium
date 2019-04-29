package com.example.com.literarium;

import android.content.Context;
import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

public class GetBookDataTask extends AsyncTask {

    private Context ref;

    private Document xmlContent;  // content returned by the server

    private HttpRequest httpRequest;

    private int bookId;

    public GetBookDataTask(Context ref) {
        this.ref = ref;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        String requestUrl = URLRequestFormatter.format(RequestType.SEARCH_BOOKS);

        httpRequest = new HttpRequest(requestUrl, HttpRequest.HttpRequestMethod.GET);
        httpRequest.send();
        xmlContent = httpRequest.getResult();

        Book book = null;

        try {
            book = XmlDataParser.parseBook(xmlContent, xmlContent.getDocumentElement());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return book;
    }

    @Override
    protected void onPostExecute(Object o) {

        ShowBookActivity concreteActivity = (ShowBookActivity)ref;

        ((ShowBookActivity) ref).loadBookData((Book)o);
    }
}
