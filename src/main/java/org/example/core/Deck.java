package org.example.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Deck<T extends Card>{
    private final List<T> cards = new ArrayList<>();

    protected void initializeDeck(){
        for(Suit suit : Suit.values()){
            for(Rank rank : getValidRanks()){
                cards.add(createCard(suit,rank));
            }
        }
    }

    protected abstract T createCard(Suit suit, Rank rank);

    protected List<Rank> getValidRanks(){
        return Arrays.asList(Rank.values());
    }

    public final T draw(){
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(cards.size() - 1);
    }

    public final List<T> draw(int n){
        if(n <= 0 || cards.isEmpty()){
            return Collections.emptyList();
        }

        int cardsToDraw = Math.min(n,cards.size());
        List<T> drawn = new ArrayList<>(cardsToDraw);

        for(int i = 0; i < cardsToDraw; i++){
            drawn.add(cards.remove(cards.size() - 1));
        }

        return drawn;
    }

    public final void shuffle(){
        Collections.shuffle(cards);
    }

    public final int getSize() {
        return cards.size();
    }

    public final boolean isEmpty() {
        return cards.isEmpty();
    }

    public final void reset(){
        cards.clear();
        initializeDeck();
        shuffle();
    }
}
