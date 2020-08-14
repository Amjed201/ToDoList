package com.example.todolisttest;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NotesDatabase extends RoomDatabase {
    private static NotesDatabase instance;


    public abstract NoteDao noteDao();


    public static synchronized NotesDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NotesDatabase.class, "Notes Database")
                    .fallbackToDestructiveMigration()
//                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }


//    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            new populateDbAsyncTask(instance).execute();
//        }
//    };
//
//    private static class populateDbAsyncTask extends AsyncTask<Void, Void, Void> {
//        private NoteDao noteDao;
//
//        private populateDbAsyncTask(NotesDatabase db) {
//            noteDao = db.noteDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            noteDao.insert(new Note("Title 1", "des 1", "1"));
//            noteDao.insert(new Note("Title 2", "des 2", "2"));
//            noteDao.insert(new Note("Title 3", "des 3", "3"));
//            return null;
//        }
//    }
}
