package no.oslomet.cs.algdat;


////////////////// class DobbeltLenketListe //////////////////////////////


import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;



public class DobbeltLenketListe<T> implements Liste<T> {
    public static void main(String[] args){
        String[] s1 = {}, s2 = {"A"}, s3 = {null,"A",null,"B",null};
        DobbeltLenketListe<String> l1 = new DobbeltLenketListe<>(s1);
        DobbeltLenketListe<String> l2 = new DobbeltLenketListe<>(s2);
        DobbeltLenketListe<String> l3 = new DobbeltLenketListe<>(s3);
        System.out.println(l1.toString() + " " + l2.toString()
                + " " + l3.toString() + " " + l1.omvendtString() + " "
                + l2.omvendtString() + " " + l3.omvendtString());

    }

    /**
     * Node class
     *
     * @param <T>
     */
    private static final class Node<T> {
        private T verdi;                   // nodens verdi
        private Node<T> forrige, neste;    // pekere

        private Node(T verdi, Node<T> forrige, Node<T> neste) {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        private Node(T verdi) {
            this(verdi, null, null);
        }
    }

    // instansvariabler
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;         // antall endringer i listen

    public DobbeltLenketListe() {
        hode = hale = null;
        endringer = 0;
        antall = 0;

    }

    public DobbeltLenketListe(T[] a) {
        //Lage en dobbeltlenketliste med verdiene fra tabellen a
        /*
            Krav-> Tabell kan ikke:
                være tom-> kaste exception
                if(a == 0)-> Sjekk om tabellen er null, hvis ikke -> finn første ikke-null-verdi
                else->hvis kun nullverdier-> returnerer tom tabell
                    //
            tom-> kaste
         */

        this(); //Bruker konstruktøren over-> nullstiller
        Objects.requireNonNull(a, "Tabellen inneholder null-verdier!"); //tabellen a kan ikke være null

        if (a.length != 0) {     //Dersom tabellen er tom? Sjekk-> loop gjennom
            // looper gjennom tabellen for å finne ikke-nullverdi
            //
            int i = 0;
            for(; i < a.length && a[i] == null; i++);
                //returnerer tom tabell, dersom den finner bare null-verdier
                if (i == a.length) return;

            Node<T> p = hode = new Node<>(a[i], null, null);
            antall = 1;

            for (i++; i < a.length; i++) {
                if (a[i] != null) {
                    p = p.neste = new Node<>(a[i], p, null);
                    antall++; //øker med antall for hver ny node
                }
            }
            hale = p;

        }
    }


    public Liste<T> subliste(int fra, int til){
        throw new UnsupportedOperationException();
    }

    @Override
    public int antall() {
        return antall; //returner antall i listen
    }

    @Override
    public boolean tom() {
        if(antall == 0){ //tom tabell
            return true;
        }
        return false;
    }

    @Override
    public boolean leggInn(T verdi) {
        Objects.requireNonNull("Ikke tillat med null-verdier!");
    }

    @Override
    public void leggInn(int indeks, T verdi) {

    }

    @Override
    public boolean inneholder(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T hent(int indeks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indeksTil(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T fjern(int indeks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void nullstill() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        //Definerer en Stringjoiner, for å klare å skille mellom tegnene, få det i denne formatet [,]
        StringJoiner sj = new StringJoiner(",","[","]");
        Node<T> p = hode;
        //Loope gjennom alle nodene som er forskjellige fra null-> add en node med teksformatet over
        for(; p!= null; p = p.neste)
            sj.add(p.verdi.toString());
        //returner Stringbuilderen tilslutt
        return sj.toString();





    }

    public String omvendtString() {
        //For å travarsere arrayet er jeg litt usikker, men jeg antar å å bruke samme format som i String toString() metoden, bare bytte om litt på rekkefølge
        //Definerer en Stringjoiner, for å klare å skille mellom tegnene, få det i denne formatet [,]
        StringJoiner sj = new StringJoiner(",","[","]");

        Node<T> p = hode;
        for(;p!=null;p=p.forrige)
            sj.add(p.verdi.toString());
        return sj.toString();
        //Bytte om på neste og forrige, så man går baklengs, i motsetning til forlengs

    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    public Iterator<T> iterator(int indeks) {
        throw new UnsupportedOperationException();
    }

    private class DobbeltLenketListeIterator implements Iterator<T>
    {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator(){
            denne = hode;     // p starter på den første i listen
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        private DobbeltLenketListeIterator(int indeks){
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext(){
            return denne != null;
        }

        @Override
        public T next(){
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(){
            throw new UnsupportedOperationException();
        }

    } // class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new UnsupportedOperationException();
    }


// class DobbeltLenketListe
}