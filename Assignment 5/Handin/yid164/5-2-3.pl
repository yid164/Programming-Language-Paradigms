% Name: Yinsheng Dong
% Student Number: 11148648
% NSID: yid164
% Lecture Section: CMPT 340


%Problem2

% married_to functions
married_to(elizabeth, philip).
married_to(philip, elizabeth).
married_to(diana, charles).
married_to(charles, diana).
married_to(charles, camilla).
married_to(camilla, charles).
married_to(william, catherine).
married_to(catherine, william).

% child_of functions
child_of(charles, philip).
child_of(charles, elizabeth).
child_of(william, charles).
child_of(william, diana).
child_of(harry, charles).
child_of(harry, diana).
child_of(george, william).
child_of(george, catherine).
child_of(charlotte, william).
child_of(charlotte, catherine).

%male functions
male(philip).
male(charles).
male(william).
male(harry).
male(george).

%female functions
female(elizabeth).
female(diana).
female(camilla).
female(catherine).
female(charlotte).



% Prroblem3

% need helper function to deal with these function
% helper function for getting parents
parents_of(Child, Mother, Father):-
    child_of(Child, Mother),
    child_of(Child, Father),
    female(Mother),
    male(Father).

% helper function for getting sisters
sisters_of(Sister, Sibling):-
    female(Sister),
    not(Sister == Sibling),
    parents_of(Sister, Mother, Father),
    parents_of(Sibling, Mother, Father).

% helper function for getting brothers
brother_of(Brother, Sibling):-
    male(Brother),
    not(Brother == Sibling),
    parents_of(Brother, Mother, Father),
    parents_of(Sibling, Mother, Father).

% helper function for getting siblings
siblings_of(Sibling1, Sibling2):-
    not(Sibling1 == Sibling2),
    parents_of(Sibling1, Mother, Father),
    parents_of(Sibling2, Mother, Father).

% helper function for getting siblings in law
sibling_in_law(SiblingInLaw, Person):-
    siblings_of(Sibling, Person),
    married_to(Sibling, SiblingInLaw).

sibling_in_law(SiblingInLaw, Person):-
    married_to(Person, Spouse),
    siblings_of(Spouse, SiblingInLaw).

%(a) aunt_of(person1, person2).
aunt_of(Aunt, Person):-
    female(Aunt),
    child_of(Person, Parent),
    sisters_of(Aunt, Parent).
aunt_of(Aunt, Person):-
    female(Aunt),
    child_of(Person, Parent),
    brother_of(Uncle, Parent),
    married_to(Aunt, Uncle).


%(b) grandchild_of(person1, person2).
grandchild_of(GrandParent, Person):-
    child_of(Parent, GrandParent),
    child_of(Person, Parent).


%(c) mother_of(person1, person2).
mother_of(Mother, Child):-
    parents_of(Child, Mother, _).


%(d) stepmother_of(person1, person2).
stepmother_of(StepMother, Child):-
    parents_of(Child, Mother, Father),
    not(Mother==StepMother),
    married_to(StepMother, Father).


%(e) nephew_of(person1, person2).
nephew_of(Newphew, Person):-
    male(Newphew),
    siblings_of(Sibling, Person),
    child_of(Newphew, Sibling).

nephew_of(Newphew, Person):-
    male(Newphew),
    parents_of(Person, Mother, Father),
    parents_of(Sibling, Mother, Father),
    not(Sibling == Person),
    married_to(Sibling, SiblingInLaw),
    not(child_of(Newphew, Sibling)),
    child_of(Newphew, SiblingInLaw).


%(f) mother_in_law_of(person1, person2).
mother_in_law_of(MotherInLaw, Person):-
    married_to(Person, Spouse),
    parents_of(Spouse, MotherInLaw, _).


%(g) brother_in_law_of(person1, person2).
brother_in_law_of(BrotherInLaw, Person):-
    male(BrotherInLaw),
    sibling_in_law(BrotherInLaw, Person).


%(h)ancestor_of(person1, person2).
ancestor_of(Ancestor, Person):-
    child_of(Person, Ancestor).
ancestor_of(Ancestor, Person):-
    child_of(Person, Parent),
    ancestor_of(Ancestor, Parent).
