package me.davidllorca.popularmovies.data.remote;

/**
 * Created by david on 9/4/18.
 */

public interface AsyncTaskListener<T> {

    void onTaskStarted();

    void onTaskCompleted(T result);

}
