Genetic Code
============

This is a (very) simple genetic code / genetic algorithm simulator / sandbox.

There's a field with walls, food and organisms on it. Those organisms behave according to a genetic program. They eat if they find food, they move, they die when they run out of energy.

Screenshot
----------
![Screenshot](http://www.abload.de/img/screenshot9tpln.png)

The program
-----------
Each organism's program consists of single instructions such as:
* go one step forward
* go two steps forward
* turn left
* turn right
* sleep
* if there is food / a wall / another organism in front of you, skip the next instruction
* etc.

When these organisms replicate, there's a certain propability that the new organisms code, it's "genes" differ slightly from it's parent organism's code:
* a single instruction could be changed
* a single instruction could be inserted
* a single instruction could be removed
That's **mutation**.

If an organism is not able to find enough food it dies. That's **selection**.
Theory is that these two ingredients are enough to provoke ***evolution***.

Observations
------------
The first few thousand steps you can watch the typical fluctuations of a predator-prey-model. The organisms proliferate until there's not enough food anymore and the organisms die until there are barely any left. The food regrows the cycle begins again.

Up until a certain point you will only see "move forward" instructions in the organisms genetic code. Any other instruction, say a "sleep" only hinders the organism; a "turn left" is usually fatal since moving around the ever same pixel is not very prone to find food.

The genetic code grows longer over time. That's just because it can - there's no disadvantage in having a program "forward forward" opposed to "forward".

When the genetic code reaches a certain length, single "turn" instructions become less fatal. A big enough cirle can provide an organism with just enough food to survive or eventually replicate and pass on it's genes.

This makes it possible for the organism to develope another "turn" instruction, usually in the opposite direction, resulting in a diagonal movement. This is interesting:

Diagonal movement has advantages over horizontal or vertical movement: The organisms don't get stuck at walls so easily which increases their life expectancy. This is an example for evolution towards better adjustment to their environment.

The "turn" instructions tend to be fatal for less developed organisms but provide vital functionality for higher developed organisms. This could serve as an example for the emergence of complex structures in biology, like the eye, which is not believed to have evolved gradually.

Just to make it clear: I do not claim that this program represents actual biologial processes accurately or anyhow at all. It just copies some processes from the real world -- and happens to come up with some similar results.

TODO
----

* Add documentation about commands.
* Let the user set immutable parameters (like field size) upfront.
* Let the user set the initial population
* Add an "About" button
* Add a "License" button

License
-------
This program is released under the terms of the Simplified BSD License.

(c) 2013, Tim Wiederhake
