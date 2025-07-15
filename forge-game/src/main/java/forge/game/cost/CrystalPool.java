/*
 * Forge: Magic The Gathering © 2021 - current Forge Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Modifications for Final Fantasy TCG © 2025 OpenAI
 */
package forge.game.cost;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Pool of Crystal resources.
 */
public class CrystalPool {
    private final EnumMap<CrystalElement, Integer> amounts = new EnumMap<>(CrystalElement.class);

    /**
     * Adds amount of element to this pool.
     *
     * @param element the element to add
     * @param amount  amount to add
     */
    public void add(CrystalElement element, int amount) {
        if (amount <= 0 || element == null) {
            return;
        }
        amounts.merge(element, amount, Integer::sum);
    }

    /**
     * Returns amount of element in this pool.
     *
     * @param element the element to query
     * @return amount present
     */
    public int get(CrystalElement element) {
        return amounts.getOrDefault(element, 0);
    }

    /**
     * Checks if this pool can pay provided cost pool. Crystal Points of any
     * element can be used to cover missing elements if the total amount is
     * sufficient.
     *
     * @param costPool the cost to check
     * @return true if enough crystals are present
     */
    public boolean canPay(CrystalPool costPool) {
        return total() >= costPool.total();
    }

    /**
     * Pays the provided cost pool if possible.
     *
     * @param costPool the cost to pay
     * @return true if payment succeeded
     */
    public boolean pay(CrystalPool costPool) {
        if (!canPay(costPool)) {
            return false;
        }

        // First use matching elements
        int remaining = costPool.total();
        for (Map.Entry<CrystalElement, Integer> e : costPool.amounts.entrySet()) {
            int use = Math.min(get(e.getKey()), e.getValue());
            if (use > 0) {
                amounts.merge(e.getKey(), -use, Integer::sum);
                if (amounts.get(e.getKey()) <= 0) {
                    amounts.remove(e.getKey());
                }
                remaining -= use;
            }
        }

        // Pay any remaining cost from other elements
        if (remaining > 0) {
            for (Map.Entry<CrystalElement, Integer> e : amounts.entrySet()) {
                if (remaining <= 0) {
                    break;
                }
                int use = Math.min(e.getValue(), remaining);
                e.setValue(e.getValue() - use);
                remaining -= use;
            }
            amounts.entrySet().removeIf(entry -> entry.getValue() <= 0);
        }

        return true;
    }

    /**
     * Total amount of Crystal Points in this pool.
     *
     * @return sum of all elements
     */
    public int total() {
        int sum = 0;
        for (int v : amounts.values()) {
            sum += v;
        }
        return sum;
    }

    /**
     * Removes all crystals from this pool.
     */
    public void empty() {
        amounts.clear();
    }

    /**
     * Creates a deep copy of this pool.
     *
     * @return new CrystalPool with identical amounts
     */
    public CrystalPool copy() {
        CrystalPool cp = new CrystalPool();
        cp.amounts.putAll(this.amounts);
        return cp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrystalPool)) {
            return false;
        }
        CrystalPool other = (CrystalPool) o;
        return Objects.equals(amounts, other.amounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amounts);
    }

    @Override
    public String toString() {
        if (amounts.isEmpty()) {
            return "[]";
        }
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (Map.Entry<CrystalElement, Integer> e : amounts.entrySet()) {
            joiner.add(e.getKey() + "=" + e.getValue());
        }
        return joiner.toString();
    }
}
