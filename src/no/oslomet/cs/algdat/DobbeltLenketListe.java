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

    private static void fraTilKontroll(int antall,int fra, int til){
        //Tre intervaller vi må utelukkke:
        if(fra<0){ //Fra er negativt -> utenfor tabellen

        }
        if (fra < 0)                                  // fra er negativ-> ikke innenfor intervallet
            throw new IndexOutOfBoundsException
                    ("Fra: (" + fra + ") er negativ!");

        if (til > antall)                          // til er utenfor tabellen-> ikke innenfor intervallet
            throw new IndexOutOfBoundsException
                    ("Til: (" + til + ") > antall(" + antall + ")");

        if (fra > til)                                // fra er større enn til -> ikke innenfor intervallet
            throw new IllegalArgumentException
                    ("Fra: (" + fra + ") > Til: (" + til + ") - illegalt intervall!");
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
        return p;
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
            hale = p; //hale = siste node

        }
    }

    public Liste<T> subliste(int fra, int til){
        fraTilKontroll(antall,fra,til); //sjekker om intervallet er gyldig
        DobbeltLenketListe<T> liste = new DobbeltLenketListe<>(); //ny liste
        Node<T> p = finnNode(fra); //finner noden

        for(int i = fra; i<til; i++){
            liste.leggInn(p.verdi);
            p = p.neste;
        }
        return liste;

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
        Objects.requireNonNull(verdi);


        if (hode == null && hale == null && antall == 0) { //tom liste
            hode = new Node<T>(verdi, null, null);
            hale = hode;
        } else { //Hva skjer dersom listen ikke er tom?
            hale = hale.neste = new Node<>(verdi, hale, null);
        }
        antall++;
        endringer++;
        return true;
        /*if(verdi == null){
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
        return true;*/
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        Objects.requireNonNull(verdi, "Ikke tillatt med nullverdier!");
        indeksKontroll(indeks, true);
        //Fire tilfeller:
        //1. if(tom()){ //tom liste
        if(tom()){
            hode = hale = new Node<>(verdi, null,null);
        }
         /*2. else if(indeks == 0){
               sett inn ny verdi på starten av tabellen
            }
         */
        else if(indeks == 0){
            hode = hode.forrige = new Node<>(verdi, null,hode);
        }
        /*3. else if(indeks == antall){
           Sett inn ny verdi på slutten av tabellen
        }
         */
        else if(indeks == antall){
            hale = hale.neste = new Node<>(verdi,hale,null);
        }
        else{
            Node<T> p = finnNode(indeks);
            p.forrige = p.forrige.neste = new Node<>(verdi,p.forrige,p);
        }
        /*siste. else{
               sett inn ny verdi
            }
            øk antall og endringer
         */
        antall++;
        endringer++;

    }

    @Override
    public boolean inneholder(T verdi) {
        return indeksTil(verdi) != -1;
    }

    @Override
    public T hent(int indeks) {
        indeksKontroll(indeks,false);
        return finnNode(indeks).verdi;
    }

    @Override
    public int indeksTil(T verdi) {
        if(verdi == null){ //Hvis tabellen inneholder nullverdier-> ikke gyldig
            return -1;
        }

        Node<T> nyNode = hode;
        for(int i = 0; i<antall; i++,nyNode = nyNode.neste){
            if(Objects.equals(nyNode.verdi,verdi))
                return i;
            continue;
        }
        return -1;
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        //Objects.requireNonNull("Tabellen inneholder null-verdier"); //feilmld hvis tabellen inneholder nullverdier
        Objects.requireNonNull(nyverdi,"Ikke tillatt med nullverdier!");
        indeksKontroll(indeks,false);
        Node<T> p = finnNode(indeks);

        //Bytter ut gammelverdi med ny verdi, hver gang det skjer må vi øke endringer med 1
        T gammelVerdi = p.verdi;
        p.verdi = nyverdi;
        endringer++;
        return gammelVerdi;
    }


    @Override
    public boolean fjern(T verdi) {
        if(verdi==null){
            return false;
        }
        Node<T>node = hode;
        if(verdi.equals(node.verdi)){
            if(node.neste == null){
                hode=hale=null;
            }else {
                hode=node.neste;
                hode.forrige=null;
            }
            antall--;
            endringer++;
            return true;
        }
        node = hale;
        if(verdi.equals(node.verdi)){
            hale=node.forrige;
            hale.neste=null;
            antall--;
            endringer++;
            return true;
        }
        node = hode.neste;
        for(; node != null; node = node.neste){
            if(verdi.equals(node.verdi)){
                node.forrige.neste = node.neste;
                node.neste.forrige = node.forrige;
                antall--;
                endringer++;
                return true;
            }
        }
        return false;

    }

    @Override
    public T fjern(int indeks) {
        indeksKontroll(indeks, false);
        //Utføre først indekskontrol, for å sjekke om intervallet er gyldig
        indeksKontroll(indeks,false);
        Node<T> node = hode;
        T verdi;
        //fire tilfeller: den første fjernes, den siste fjernes, kun en verdi i tabellen, en verdi mellom to andre fjernes
        //1. if(indeks == 0){
        // første verdi skal fjernes
        //}
        if(indeks == 0){
            verdi = node.verdi;
            if(node.neste==null){
                hode=hale = null;
            }else {
                hode=node.neste;
                hode.forrige=null;
            }
        }

        else if(indeks == antall-1){ //Hvis siste verdi skal fjernes
            verdi = hale.verdi;
            node=hale;
            hale=node.forrige;
            hale.neste=null;
        }

        else {
            for(int i = 0; i<indeks; i++){  //Hvis en verdi mellom hode og hale fjernes
                node=node.neste;
            }
            verdi = node.verdi;
            node.forrige.neste=node.neste;
            node.neste.forrige=node.forrige;
        }

        //Mink antall (vi fjerner verdier), og øke endringer(fjerne = endring)
        antall--;
        endringer++;
        return verdi;
    }
    @Override
    public void nullstill() {
        this.hode = null;
        this.hale = null;
        antall = 0;
        endringer++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        if (!tom())
        {
            sb.append(hode.verdi);
            for (Node<T> p = hode.neste; p != null; p = p.neste)
            {
                sb.append(',').append(' ').append(p.verdi);
            }
        }

        sb.append(']');
        return sb.toString();
    }


    public String omvendtString() {
        StringBuilder sb = new StringBuilder(); //lager stringbuilder
        sb.append('['); //Adder første klammeparantes

        if (!tom()) //Dersom listen ikke er tom, altså inneholder verdier:
        {
            sb.append(hale.verdi); //Legg først verdi hale , så legger du de andrre i forhold til den

            for (Node<T> p = hale.forrige; p != null; p = p.forrige)
            {
                sb.append(',').append(' ').append(p.verdi); //Tekstformat og legg inn
            }
        }

        sb.append(']');
        return sb.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new DobbeltLenketListeIterator();
    }

    public Iterator<T> iterator(int indeks) {
        indeksKontroll(indeks,false);
        return new DobbeltLenketListeIterator(indeks);
    }

    private class DobbeltLenketListeIterator implements Iterator<T>
    {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator(){
            denne = hode;     // p starter på den første i listen
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer som blir foretatt
        }

        private DobbeltLenketListeIterator(int indeks){
            //indeksKontroll(indeks,false);
            denne = finnNode(indeks); //denne settes til noden med gitt indeks
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        @Override
        public boolean hasNext(){
            return denne != null;
        }

        @Override
        public T next(){
            if(iteratorendringer != endringer){
                throw new ConcurrentModificationException("Listen er endret");
            }

            if(!hasNext()){
                throw new NoSuchElementException("Ingen verdier!");
            }

            fjernOK = true; //Verdi fjernet
            T nåværendeVerdi = denne.verdi; //Holde på verdien "denne.verdi"
            denne=denne.neste; // flytter "denne" noden til neste
            return nåværendeVerdi; //returnerer hjelpevariabelen som ble brukt for å holde på "denne"
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