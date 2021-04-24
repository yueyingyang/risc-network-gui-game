ECE 651: RISC
======================================

![pipeline](https://gitlab.oit.duke.edu/yy252/ece651-spr21-group2-risc/badges/master/pipeline.svg)

![coverage](https://gitlab.oit.duke.edu/yy252/ece651-spr21-group2-risc/badges/master/coverage.svg?job=test)

## Coverage
[Detailed coverage](https://yy252.pages.oit.duke.edu/ece651-spr21-group2-risc/dashboard.html)

## v3 group3 spec
### In short
Support to buy products during the game. The products are independent, and can be used after purchased and before the game ends. 

### In detail
Below are 4 types of products sold in the Product Store(the number after each product is the food resources cost, user input is the required fields when user want to use this prop, listed for implementation):

1. Missile(100)：
   - Explanation: clear the army on the target territory, but doesn't modify the owner directly (large possibly the player who uses it could attack by an extra unit to get this territory)
   - User input: to territory
   - If multiple players send missiles to the same territory or one player send missiles to the same territory, the effect is the same
   - The Missile will be used after move (including move in attack, spy move).
   - The program will show the players that use missile when resolving combat
2. Ship(50)：
   - Explanation: allow players to attack non-adjacent enemy territories
   - User input: to territory, soldier type, soldier number
   - The food resource cost is the same for use or not use ship, i.e. food resource cost = number of units
   - The program will show the players that use ship when resolving combat
3. Shield(defender)(50):
   - Explanation: The player can use shield on his own territory. The shield will add 3 bonus to each soldier in the defender when resolving combat.
   - User input: to territory
   - The program will show the players that use shield when resolving combat
4. Sword(attacker)(50)：
   - Explanation: The player can use sword on enemy's territory when attacking that territory. The sword will add 3 bonus to each soldier in attacker. If multiple players attacks the same territory, the player 
   using sword can still have the bonus when he becomes the defender.
   - User input: to territory
   - The program will show the players that use sword when resolving combat

## UX principles v2
(https://gitlab.oit.duke.edu/yy252/ece651-spr21-group2-risc/-/blob/master/UX/UX_summary.pdf)

### Reference
<div>Icons used in the store page are made by <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>