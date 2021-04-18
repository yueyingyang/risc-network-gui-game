ECE 651: RISC
======================================

![pipeline](https://gitlab.oit.duke.edu/yy252/ece651-spr21-group2-risc/badges/master/pipeline.svg)

![coverage](https://gitlab.oit.duke.edu/yy252/ece651-spr21-group2-risc/badges/master/coverage.svg?job=test)

## Coverage
[Detailed coverage](https://yy252.pages.oit.duke.edu/ece651-spr21-group2-risc/dashboard.html)

## v3 group3 spec
### In short
Support to buy props during the game. The props are game independent (i.e. can only be used in this turn and would expire after the game ends)

### In detail
Below are 4 types of props sold in the Props Store(the number after each prop is the food resources cost, user input is the required fields when user want to use this prop, listed for implementation):

1. Missile(100)：
   - Explanation: clear the army on the target territory, but doesn't modify the owner directly (large possibly the player who uses it could attack by an extra unit to get this territory)
   - User input: to territory
2. Ship(50)：
   - Explanation: move units without rules (no adjacent rule)
   - User input: to territory, soldier type, soldier number 
3. Shield(defender)(30):
   - Explanation: will increase the defense level of player's own territory = increase bonus(3) on defender buffer
   - User input: to territory
4. Sword(attacker)(30)：
   - Explanation: will increase the attack level if you attacked this territory in this turn = increase bonus(3) on attack buffer
   - User input: to territory

### Other implementation details:
There will be a **Props Store** modal / page, which helps:
1. Show the props owned
2. Show the props you can buy and could select the purchase amount for each props
3. Calculate the total cost amount

## UX principles v2
(https://gitlab.oit.duke.edu/yy252/ece651-spr21-group2-risc/-/blob/master/UX/UX_summary.pdf)
