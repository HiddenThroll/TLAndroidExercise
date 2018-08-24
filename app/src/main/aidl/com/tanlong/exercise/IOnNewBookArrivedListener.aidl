// IOnNewBookArrivedListener.aidl
package com.tanlong.exercise;

// Declare any non-default types here with import statements
import com.tanlong.exercise.aidl.Book;

interface IOnNewBookArrivedListener {

    void onNewBookArrived(in Book newBook);

}
