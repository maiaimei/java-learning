# Sequenced Collections

## JEP 431: Sequenced Collections - JDK 21

1. **New Interfaces Introduction**

   ```java
   // Three new interfaces added to the collection framework:
   interface SequencedCollection<E> extends Collection<E>
   interface SequencedSet<E> extends Set<E>, SequencedCollection<E>
   interface SequencedMap<K,V> extends Map<K,V>
   ```

2. **SequencedCollection Methods**

   ```java
   interface SequencedCollection<E> extends Collection<E> {
       // New method
       SequencedCollection<E> reversed();
       
       // Methods promoted from Deque
       void addFirst(E);
       void addLast(E);
       E getFirst();
       E getLast();
       E removeFirst();
       E removeLast();
   }
   ```

3. **SequencedSet Methods**

   ```java
   interface SequencedSet<E> extends Set<E>, SequencedCollection<E> {
       SequencedSet<E> reversed();    // covariant override
   }
   ```

4. **SequencedMap Methods**

   ```java
   interface SequencedMap<K,V> extends Map<K,V> {
       // New methods
       SequencedMap<K,V> reversed();
       SequencedSet<K> sequencedKeySet();
       SequencedCollection<V> sequencedValues();
       SequencedSet<Entry<K,V>> sequencedEntrySet();
       V putFirst(K, V);
       V putLast(K, V);
       
       // Methods promoted from NavigableMap
       Entry<K,V> firstEntry();
       Entry<K,V> lastEntry();
       Entry<K,V> pollFirstEntry();
       Entry<K,V> pollLastEntry();
   }
   ```

5. **Retrofitted Classes**

   - List now extends SequencedCollection
   - Deque now extends SequencedCollection
   - LinkedHashSet implements SequencedSet
   - SortedSet extends SequencedSet
   - LinkedHashMap implements SequencedMap
   - SortedMap extends SequencedMap

6. **New Utility Methods**

   ```java
   // New unmodifiable wrapper methods in Collections
   Collections.unmodifiableSequencedCollection(sequencedCollection)
   Collections.unmodifiableSequencedSet(sequencedSet)
   Collections.unmodifiableSequencedMap(sequencedMap)
   ```

7. **Usage Examples**

   ```java
   // Getting last element (previously cumbersome)
   // Old way
   list.get(list.size() - 1);
   // New way
   list.getLast();
   
   // Reverse order streaming (previously difficult)
   // Old way for LinkedHashSet - no direct support
   // New way
   linkedHashSet.reversed().stream()
       
   // Example of enhanced operations
   SequencedSet<String> set = new LinkedHashSet<>();
   set.addLast("last");
   set.addFirst("first");
   String first = set.getFirst();
   String last = set.getLast();
   
   // Reverse iteration
   for (String element : set.reversed()) {
       System.out.println(element);
   }
   ```

8. **Usage Guidelines**

   * **Collection Choice**
  * Use SequencedCollection when order matters
     * Consider SequencedSet for unique ordered elements
     * Use SequencedMap for ordered key-value pairs
   
* **Operation Selection**
     * Use getFirst()/getLast() instead of index-based access
  * Leverage reversed() for reverse iteration
     * Use new utility methods for unmodifiable views
   
   * **Migration Strategy**
  * Update interfaces to use new sequence types
     * Refactor code to use new methods
  * Consider performance implications