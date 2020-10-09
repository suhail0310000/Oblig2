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
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>();
        System.out.println(liste.toString() + " " + liste.omvendtString());
        for (int i = 1; i <= 3; i++)
        {
            liste.leggInn(i);
            System.out.println(liste.toString() + " " + liste.omvendtString());
        }


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

    private Node<T> finnNode(int indeks){
        Node<T> p;
       //if(antall/2>indeks) grav fra hode og loop opp til indeksen
        if(antall/2 < indeks){
            p = hale; //Looper gjennom fra høye side mot indeksen, via halen
            for(int i = antall-1; i>indeks; i--){
                p = p.forrige;
            }
        }
        else{
            p = hode; // else  loop fra halen ned til indeksen
            for(int i = 0; i<indeks; i++) {
                p = p.neste;
            }
        }
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

        if (a.length != 0) {     //Dersom tabellen ikke er tom? Sjekk-> loop gjennom
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

        if(verdi == null){
            Objects.requireNonNull(verdi,"Ikke tillat med null-verdier!");
        }
        else if(tom()){
            hode = hale = new Node<>(verdi,null,null);
        }
        else{
            hale = hale.neste = new Node<>(verdi,hale,null);
            antall++; //øker antall med en for hver gang det legges en ny node til
            endringer++;
        }
        return true;
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
        indeksKontroll(indeks,false);
        return finnNode(indeks).verdi;
    }

    @Override
    public int indeksTil(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        Objects.requireNonNull("Tabellen inneholder null-verdier"); //feilmld hvis tabellen inneholder null-verdier

        indeksKontroll(indeks,false);

        Node<T> p = finnNode(indeks);

        //Bytter ut gammelverdi med ny verdi, hver gang det skjer må vi øke endringer med 1
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

        Node<T> p = hale;
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