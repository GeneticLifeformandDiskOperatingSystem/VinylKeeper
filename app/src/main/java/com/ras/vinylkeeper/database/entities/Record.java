package com.ras.vinylkeeper.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.ras.vinylkeeper.database.RecordDatabase;
import com.ras.vinylkeeper.database.typeConverter.LocalDateTypeConverter;

import java.time.LocalDateTime;
import java.util.Objects;

@TypeConverters(LocalDateTypeConverter.class)
@Entity(tableName = RecordDatabase.RECORD_TABLE)
public class Record {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int owner;
    private String artist;
    private String album;
    private int year;
    private double rating;
    private LocalDateTime dateAdded;

    public Record(String artist, String album, int year, double rating) {
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.rating = rating;
        this.dateAdded = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return id == record.id && owner == record.owner && Double.compare(rating, record.rating) == 0 && Objects.equals(artist, record.artist) && Objects.equals(album, record.album) && Objects.equals(year, record.year) && Objects.equals(dateAdded, record.dateAdded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner, artist, album, year, rating, dateAdded);
    }
}
