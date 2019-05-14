package com.example.com.literarium;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.geoLocalization.GeoLocalizationActivity;
import com.example.com.localDB.SaveBookTask;
import com.example.com.parsingData.parseType.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShowBookActivity extends Activity {

    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookPublishDate;
    private TextView bookDescription;

    private ImageView bookCover;

    private ImageButton saveBookButton;

    /**
     * complete data of the book
     * this is necessary to make book-saving possible
     */
    private Book bookObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_layout);

        Bundle data = getIntent().getExtras();

        saveBookButton = findViewById(R.id.saveBookButton);
        bookTitle = findViewById(R.id.bookTitle);
        bookAuthor = findViewById(R.id.bookAuthor);
        bookPublishDate = findViewById(R.id.bookPublishDate);
        bookDescription = findViewById(R.id.bookDescription);
        bookDescription.setSelected(true);
        bookCover = findViewById(R.id.bookCover);

        GetBookDataTask getBookDataTask = new GetBookDataTask(this, /*data.getInt("bookId")*/923832);
        getBookDataTask.execute();
    }

    public void saveBook(View v) {

        ArrayList<Book> toSaveBookList = new ArrayList<>();
        toSaveBookList.add(bookObj);

        SaveBookTask saveBookTask = new SaveBookTask(this, toSaveBookList);
        saveBookTask.execute();
    }

    public void loadBookData(com.example.com.parsingData.parseType.Book b) {

        bookObj = b;

        bookTitle.setText(b.getTitle());
        bookAuthor.setText(b.getAuthor().getName());
        bookPublishDate.setText(String.valueOf(b.getPublicationYear()));
        bookDescription.setHint("");
        bookDescription.setEms(b.getDescription().length());
        bookDescription.setText(Html.fromHtml(b.getDescription()));
        Picasso.get().load(b.getImageUrl()).into(bookCover);

    }

    public void handleBookSavingSuccess() {

        Toast.makeText(this, "book saved successfully!", Toast.LENGTH_SHORT).show();
    }

    public void goToGeolocalization(View v) {

        // make bundle with the data of the book to be shared
        Bundle b = new Bundle();
        b.putInt("bookId", bookObj.getId());
        b.putString("bookTitle", bookObj.getTitle());
        b.putString("bookIsbn", bookObj.getIsbn());
        b.putString("bookImageUrl", bookObj.getImageUrl());
        b.putString("bookPubYear", bookObj.getPublicationYear());
        b.putString("bookPublisher", bookObj.getPublisher());
        b.putString("bookDescription", bookObj.getDescription());
        b.putString("bookNumPages", bookObj.getNumPages());
        b.putDouble("bookRating", bookObj.getAverageRating());
        b.putParcelable("bookAuthor", bookObj.getAuthor());

        Intent i = new Intent(this, GeoLocalizationActivity.class);
        i.putExtras(b);
        startActivity(i);
    }
}