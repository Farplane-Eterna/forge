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

import com.google.common.base.Strings;
import forge.game.card.Card;
import forge.game.player.Player;
import forge.game.spellability.SpellAbility;
import forge.game.cost.PaymentDecision;

/**
 * Cost part representing payment in Crystal Points.
 */
public class CostPayCrystal extends CostPart {
    private static final long serialVersionUID = 1L;

    private final int amount;
    private int paidAmount = 0;

    /**
     * Creates a new crystal payment cost.
     *
     * @param amount number of Crystal Points required
     */
    public CostPayCrystal(String amount) {
        this.amount = Integer.parseInt(amount);
    }

    @Override
    public int paymentOrder() { return 7; }

    @Override
    public Integer getMaxAmountX(SpellAbility ability, Player payer, boolean effect) {
        return payer.getTotalCrystals();
    }

    @Override
    public String toString() {
        return "Pay " + Strings.repeat("{CP}", amount);
    }

    @Override
    public void refund(Card source) {
        // no-op for now
    }

    @Override
    public boolean canPay(SpellAbility ability, Player payer, boolean effect) {
        CrystalPool cost = new CrystalPool();
        cost.add(CrystalElement.FIRE, amount); // element irrelevant when mismatch allowed
        return payer.canPayCrystal(cost);
    }

    @Override
    public boolean payAsDecided(Player ai, PaymentDecision decision, SpellAbility ability, boolean effect) {
        CrystalPool cost = new CrystalPool();
        cost.add(CrystalElement.FIRE, amount);
        paidAmount = amount;
        return ai.payCrystal(cost);
    }

    @Override
    public <T> T accept(ICostVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
