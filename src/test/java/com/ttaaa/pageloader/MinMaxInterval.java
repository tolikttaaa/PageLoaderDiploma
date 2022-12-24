package com.ttaaa.pageloader;

import java.util.List;
import java.util.Stack;

public class MinMaxInterval<T extends Comparable<T>> {
    private final Stack<Triple<T>> firstStack;
    private final Stack<Triple<T>> secondStack;

    public MinMaxInterval(List<T> initValues) {
        secondStack = new Stack<>();
        firstStack = new Stack<>();

        initValues.forEach(this::firstStackPush);
    }

    public void append(T newValue) {
        secondStackPop();
        firstStackPush(newValue);
    }

    private void firstStackPush(T newValue) {
        if (firstStack.isEmpty()) {
            firstStack.add(new Triple<>(newValue, newValue, newValue));
        } else {
            firstStack.add(new Triple<>(firstStack.peek(), newValue));
        }
    }

    private void secondStackPush(T newValue) {
        if (secondStack.isEmpty()) {
            secondStack.add(new Triple<>(newValue, newValue, newValue));
        } else {
            secondStack.add(new Triple<>(secondStack.peek(), newValue));
        }
    }

    private void secondStackPop() {
        if (secondStack.isEmpty() && firstStack.isEmpty()) {
            return;
        }

        if (secondStack.isEmpty()) {
            while (!firstStack.isEmpty()) {
                secondStackPush(firstStack.pop().value);
            }
        }

        secondStack.pop();
    }

    public T min() {
        if (firstStack.isEmpty() && secondStack.isEmpty()) {
            return null;
        }

        if (firstStack.isEmpty()) {
            return secondStack.peek().min;
        }

        if (secondStack.isEmpty()) {
            return firstStack.peek().min;
        }

        return min(firstStack.peek().min, secondStack.peek().min);
    }

    public T max() {
        if (firstStack.isEmpty() && secondStack.isEmpty()) {
            return null;
        }

        if (firstStack.isEmpty()) {
            return secondStack.peek().max;
        }

        if (secondStack.isEmpty()) {
            return firstStack.peek().max;
        }

        return max(firstStack.peek().max, secondStack.peek().max);
    }

    private <K extends Comparable<K>> K min(K first, K second) {
        return first.compareTo(second) <= 0 ? first : second;
    }

    private <K extends Comparable<K>> K max(K first, K second) {
        return first.compareTo(second) >= 0 ? first : second;
    }

    private class Triple<K extends Comparable<K>> {
        K value;
        K min;
        K max;

        Triple (Triple<K> prevTriple, K newValue) {
            this.value = newValue;
            this.min = min(prevTriple.min, newValue);
            this.max = max(prevTriple.max, newValue);
        }

        Triple (K value, K min, K max) {
            this.value = value;
            this.min = min;
            this.max = max;
        }
    }
}
