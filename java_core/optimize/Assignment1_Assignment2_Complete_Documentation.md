# Complete Documentation: Assignment 1 & Assignment 2
## Fuzzy Logic Systems - Theory, Implementation, and Presentation Guide

---

## Table of Contents

### Part 1: Assignment 1 - Fuzzy Set Operations
1. [Introduction to Fuzzy Logic](#introduction-to-fuzzy-logic)
2. [Fuzzy Sets vs Crisp Sets](#fuzzy-sets-vs-crisp-sets)
3. [Membership Functions](#membership-functions)
4. [Triangular Membership Function](#triangular-membership-function)
5. [Universe of Discourse](#universe-of-discourse)
6. [Fuzzy Set Operations](#fuzzy-set-operations)
7. [Code Walkthrough](#code-walkthrough-assignment-1)
8. [Why Each Operation?](#why-each-operation)

### Part 2: Assignment 2 - Fuzzy Relations and Inference
1. [Introduction to Fuzzy Inference](#introduction-to-fuzzy-inference)
2. [Fuzzy Rules](#fuzzy-rules)
3. [Mamdani Inference System](#mamdani-inference-system)
4. [Rule Aggregation](#rule-aggregation)
5. [Defuzzification](#defuzzification)
6. [Code Walkthrough](#code-walkthrough-assignment-2)
7. [Complete Example Walkthrough](#complete-example-walkthrough)

---

# PART 1: ASSIGNMENT 1 - FUZZY SET OPERATIONS

## 1. Introduction to Fuzzy Logic

### What is Fuzzy Logic?

**Fuzzy Logic** is a mathematical framework that deals with reasoning that is approximate rather than precise. Unlike classical (Boolean) logic where something is either TRUE (1) or FALSE (0), fuzzy logic allows for degrees of truth between 0 and 1.

### Real-World Example

**Classical Logic (Crisp):**
- Temperature > 25°C = HOT (1)
- Temperature ≤ 25°C = NOT HOT (0)
- Problem: What about 24.9°C? Is it really "not hot"?

**Fuzzy Logic:**
- 20°C might be "hot" with degree 0.2 (20% hot)
- 25°C might be "hot" with degree 0.5 (50% hot)
- 30°C might be "hot" with degree 0.9 (90% hot)
- This is more natural and human-like reasoning!

---

## 2. Fuzzy Sets vs Crisp Sets

### Crisp (Classical) Sets

In classical set theory, an element either **belongs** or **does not belong** to a set.

**Example:**
- Set A = {temperatures > 25°C}
- 26°C ∈ A (belongs) → membership = 1
- 24°C ∉ A (doesn't belong) → membership = 0

**Visual Representation:**
```
Membership
   1 |     ┌───────
   0 |─────┘
     0    25   50  Temperature
```

### Fuzzy Sets

In fuzzy set theory, an element can **partially belong** to a set with a degree between 0 and 1.

**Example:**
- Fuzzy Set "Hot" = temperatures with varying degrees of "hotness"
- 20°C → membership = 0.2 (slightly hot)
- 25°C → membership = 0.5 (moderately hot)
- 30°C → membership = 0.9 (very hot)

**Visual Representation:**
```
Membership
   1 |        /\
   0.5|      /  \
   0  |─────/    \────
     0    25   50  Temperature
```

### Key Difference

| Aspect | Crisp Set | Fuzzy Set |
|--------|-----------|-----------|
| Membership | 0 or 1 | 0 to 1 (continuous) |
| Boundary | Sharp | Gradual |
| Example | "Age > 18" | "Young person" |
| Natural? | No | Yes |

---

## 3. Membership Functions

### What is a Membership Function?

A **Membership Function (MF)** is a mathematical function that maps each element in the universe of discourse to a membership value between 0 and 1.

**Mathematical Definition:**
```
μ_A(x): X → [0, 1]
```
Where:
- `μ_A(x)` = membership value of element x in fuzzy set A
- X = universe of discourse (all possible values)
- [0, 1] = membership range

### Why Membership Functions?

1. **Quantify Vagueness**: Convert vague concepts ("hot", "cold") into numbers
2. **Enable Computation**: Allow mathematical operations on fuzzy concepts
3. **Model Human Reasoning**: Mimic how humans think about categories

### Types of Membership Functions

1. **Triangular** - Simple, easy to understand
2. **Trapezoidal** - Has a flat top (plateau)
3. **Gaussian** - Smooth, bell-shaped curve
4. **Bell-shaped** - Generalized bell curve
5. **Sigmoidal** - S-shaped curve

---

## 4. Triangular Membership Function

### Definition

A **Triangular Membership Function** is defined by three points: `(a, b, c)` where:
- `a` = left boundary (membership = 0)
- `b` = peak point (membership = 1)
- `c` = right boundary (membership = 0)

### Mathematical Formula

```
μ(x) = {
    0,                    if x ≤ a or x ≥ c
    (x - a) / (b - a),    if a < x ≤ b
    (c - x) / (c - b),    if b < x < c
}
```

### Visual Representation

```
Membership
   1 |        /\
   0.5|      /  \
   0  |─────/    \────
      a    b    c    x
```

### Why Triangular?

**Advantages:**
1. **Simple**: Easy to understand and implement
2. **Computationally Efficient**: Fast calculations
3. **Intuitive**: Matches human thinking about categories
4. **Flexible**: Can represent various shapes by adjusting parameters

**Example in Code:**
```java
TriangularMF cold = new TriangularMF(10, 10, 25);
// Parameters: a=10, b=10, c=25
// This means:
// - Below 10°C: membership = 0
// - At 10°C: membership = 1 (peak)
// - At 25°C: membership = 0
// - Between 10-25°C: gradual decrease
```

### How It Works Step-by-Step

**Example: Temperature = 20°C with TriangularMF(10, 10, 25)**

1. Check: Is 20 ≤ 10? **No**
2. Check: Is 20 > 10 AND 20 ≤ 10? **No** (20 > 10, but 20 > 10)
3. Check: Is 20 > 10 AND 20 < 25? **Yes!**
4. Use formula: `(c - x) / (c - b) = (25 - 20) / (25 - 10) = 5/15 = 0.333`

**Result**: At 20°C, membership in "cold" set = 0.333 (33.3%)

---

## 5. Universe of Discourse

### What is Universe of Discourse?

The **Universe of Discourse (U)** is the set of all possible values that can be considered in a fuzzy system.

**Example:**
- For temperature: U = [10°C, 40°C]
- For age: U = [0, 120] years
- For height: U = [0, 300] cm

### Why Do We Need It?

1. **Define Boundaries**: Limits the range of values we consider
2. **Enable Operations**: Allows us to perform operations on entire sets
3. **Discretization**: Converts continuous values into discrete points for computation

### In Assignment 1

```java
List<Double> universe = createUniverse(10, 40, 50);
// Creates 50 points between 10 and 40
// Step size = (40 - 10) / (50 - 1) = 30/49 ≈ 0.612
// Points: 10.0, 10.612, 11.224, ..., 39.388, 40.0
```

**Why 50 points?**
- **Resolution**: More points = more accurate, but slower
- **Balance**: 50 points gives good accuracy without being too slow
- **Practical**: Enough to see smooth curves in output

### How createUniverse() Works

```java
private static List<Double> createUniverse(double lower, double upper, int resolution) {
    List<Double> universe = new ArrayList<>();
    double step = (upper - lower) / (resolution - 1);  // Calculate step size
    for (int i = 0; i < resolution; i++) {
        universe.add(lower + i * step);  // Add each point
    }
    return universe;
}
```

**Step-by-Step Example (10, 40, 5 points):**
1. step = (40 - 10) / (5 - 1) = 30 / 4 = 7.5
2. i=0: 10 + 0×7.5 = 10.0
3. i=1: 10 + 1×7.5 = 17.5
4. i=2: 10 + 2×7.5 = 25.0
5. i=3: 10 + 3×7.5 = 32.5
6. i=4: 10 + 4×7.5 = 40.0

---

## 6. Fuzzy Set Operations

### Why Do We Need Operations?

Just like in classical set theory, we need operations to:
1. **Combine Sets**: Union, Intersection
2. **Modify Sets**: Complement
3. **Compare Sets**: Difference
4. **Advanced Operations**: Algebraic product, sum

### Operation 1: Union (A ∪ B)

**Definition**: The union of two fuzzy sets is the **maximum** membership value at each point.

**Mathematical Formula:**
```
μ_(A∪B)(x) = max(μ_A(x), μ_B(x))
```

**In Code:**
```java
private static List<Double> union(List<Double> setA, List<Double> setB) {
    List<Double> result = new ArrayList<>();
    for (int i = 0; i < setA.size(); i++) {
        result.add(Math.max(setA.get(i), setB.get(i)));  // Take maximum
    }
    return result;
}
```

**Why Maximum?**
- If element belongs to A with 0.3 and B with 0.7, it belongs to (A ∪ B) with 0.7
- We take the **stronger** membership
- Logical: "x is in A OR B" → take the higher value

**Example:**
- Cold(20°C) = 0.5
- Hot(20°C) = 0.2
- Union(20°C) = max(0.5, 0.2) = **0.5**

**Visual:**
```
Cold:     /\
         /  \
        /    \
Hot:          \    /
               \  /
                \/
Union:   /\
        /  \    /
       /    \  /
            \/
```

### Operation 2: Intersection (A ∩ B)

**Definition**: The intersection of two fuzzy sets is the **minimum** membership value at each point.

**Mathematical Formula:**
```
μ_(A∩B)(x) = min(μ_A(x), μ_B(x))
```

**In Code:**
```java
private static List<Double> intersection(List<Double> setA, List<Double> setB) {
    List<Double> result = new ArrayList<>();
    for (int i = 0; i < setA.size(); i++) {
        result.add(Math.min(setA.get(i), setB.get(i)));  // Take minimum
    }
    return result;
}
```

**Why Minimum?**
- If element belongs to A with 0.3 and B with 0.7, it belongs to (A ∩ B) with 0.3
- We take the **weaker** membership
- Logical: "x is in A AND B" → both must be true, so take lower value

**Example:**
- Cold(20°C) = 0.5
- Comfortable(20°C) = 0.6
- Intersection(20°C) = min(0.5, 0.6) = **0.5**

**Visual:**
```
Cold:     /\
         /  \
        /    \
Comfortable:    /\
               /  \
              /    \
Intersection:    /\
                /  \
               /    \
```

### Operation 3: Complement (A')

**Definition**: The complement of a fuzzy set is **1 minus** the membership value.

**Mathematical Formula:**
```
μ_A'(x) = 1 - μ_A(x)
```

**In Code:**
```java
private static List<Double> complement(List<Double> set) {
    List<Double> result = new ArrayList<>();
    for (Double value : set) {
        result.add(1 - value);  // Subtract from 1
    }
    return result;
}
```

**Why 1 - value?**
- If something is 0.7 "hot", it's 0.3 "not hot"
- Logical: Complement means "opposite"
- Ensures: μ_A(x) + μ_A'(x) = 1

**Example:**
- Comfortable(25°C) = 0.8
- Complement(25°C) = 1 - 0.8 = **0.2**

**Visual:**
```
Original:    /\
            /  \
           /    \
Complement:      \    /
                 \  /
                  \/
```

### Operation 4: Algebraic Product (A · B)

**Definition**: Multiply membership values at each point.

**Mathematical Formula:**
```
μ_(A·B)(x) = μ_A(x) × μ_B(x)
```

**In Code:**
```java
private static List<Double> algebraicProduct(List<Double> setA, List<Double> setB) {
    List<Double> result = new ArrayList<>();
    for (int i = 0; i < setA.size(); i++) {
        result.add(setA.get(i) * setB.get(i));  // Multiply
    }
    return result;
}
```

**Why Multiplication?**
- More restrictive than intersection (min)
- If A=0.5 and B=0.6: min=0.5, product=0.3
- Product is always ≤ min
- Used in some fuzzy inference systems

**Example:**
- Cold(20°C) = 0.5
- Comfortable(20°C) = 0.6
- Product(20°C) = 0.5 × 0.6 = **0.3**

### Operation 5: Algebraic Sum (A + B)

**Definition**: Special sum formula that ensures result stays in [0, 1].

**Mathematical Formula:**
```
μ_(A+B)(x) = μ_A(x) + μ_B(x) - μ_A(x) × μ_B(x)
```

**In Code:**
```java
private static List<Double> algebraicSum(List<Double> setA, List<Double> setB) {
    List<Double> result = new ArrayList<>();
    for (int i = 0; i < setA.size(); i++) {
        double a = setA.get(i);
        double b = setB.get(i);
        result.add(a + b - a * b);  // Special formula
    }
    return result;
}
```

**Why This Formula?**
- Simple sum (a + b) could exceed 1 (e.g., 0.7 + 0.6 = 1.3)
- Formula ensures: result ≤ 1
- If a=0.7, b=0.6: 0.7 + 0.6 - 0.42 = **0.88** (valid!)

**Example:**
- Cold(20°C) = 0.5
- Comfortable(20°C) = 0.6
- Sum(20°C) = 0.5 + 0.6 - 0.3 = **0.8**

### Operation 6: Difference (A - B)

**Definition**: Subtract membership values, but ensure result ≥ 0.

**Mathematical Formula:**
```
μ_(A-B)(x) = max(μ_A(x) - μ_B(x), 0)
```

**In Code:**
```java
private static List<Double> difference(List<Double> setA, List<Double> setB) {
    List<Double> result = new ArrayList<>();
    for (int i = 0; i < setA.size(); i++) {
        result.add(Math.max(setA.get(i) - setB.get(i), 0));  // Ensure ≥ 0
    }
    return result;
}
```

**Why max(..., 0)?**
- Membership can't be negative
- If A=0.3 and B=0.6: 0.3 - 0.6 = -0.3 → becomes **0**

**Example:**
- Hot(30°C) = 0.8
- Comfortable(30°C) = 0.3
- Difference(30°C) = max(0.8 - 0.3, 0) = **0.5**

---

## 7. Code Walkthrough - Assignment 1

### Step-by-Step Execution

#### Step 1: Create Membership Functions

```java
TriangularMF cold = new TriangularMF(10, 10, 25);
TriangularMF comfortable = new TriangularMF(15, 25, 30);
TriangularMF hot = new TriangularMF(25, 40, 40);
```

**What happens:**
- Creates three fuzzy sets for temperature
- Each has different parameters defining its shape
- These represent linguistic variables: "cold", "comfortable", "hot"

**Why these parameters?**
- **Cold(10, 10, 25)**: Peak at 10°C, decreases to 0 at 25°C
- **Comfortable(15, 25, 30)**: Rises from 15°C, peaks at 25°C, falls to 0 at 30°C
- **Hot(25, 40, 40)**: Starts at 25°C, peaks at 40°C

#### Step 2: Create Universe

```java
List<Double> universe = createUniverse(10, 40, 50);
```

**What happens:**
- Creates 50 discrete points between 10 and 40
- These represent all possible temperature values we'll consider
- Enables set operations on entire ranges

#### Step 3: Evaluate Membership Functions

```java
for (double x : universe) {
    coldValues.add(cold.getMembership(x));
    comfortableValues.add(comfortable.getMembership(x));
    hotValues.add(hot.getMembership(x));
}
```

**What happens:**
- For each point in universe, calculates membership in each set
- Creates three lists: one for each fuzzy set
- Each list has 50 values (one per universe point)

**Example at x = 20°C:**
- cold.getMembership(20) = 0.333
- comfortable.getMembership(20) = 0.333
- hot.getMembership(20) = 0.0

#### Step 4: Perform Operations

```java
List<Double> unionColdHot = union(coldValues, hotValues);
List<Double> intersectionColdComfortable = intersection(coldValues, comfortableValues);
// ... etc
```

**What happens:**
- Applies each operation to entire sets
- Creates new fuzzy sets as results
- Each result is also a list of 50 values

#### Step 5: Get User Input

```java
double temp = scanner.nextDouble();
int index = findClosestIndex(universe, temp);
```

**What happens:**
- User enters a temperature (e.g., 22.5°C)
- Finds closest point in universe (e.g., index 20)
- Uses this index to look up operation results

#### Step 6: Display Results

```java
System.out.printf("Union (Cold ∪ Hot): %.4f\n", unionColdHot.get(index));
```

**What happens:**
- Shows membership values for input temperature
- Shows results of all operations at that temperature
- Displays membership functions across range

---

## 8. Why Each Operation?

### Why Union?

**Use Case**: "Is it cold OR hot?"
- Combines two categories
- Useful when either condition is acceptable
- Example: Air conditioning turns on if "cold OR hot" (not comfortable)

### Why Intersection?

**Use Case**: "Is it cold AND comfortable?" (rare, but shows overlap)
- Finds where two sets overlap
- Useful for finding ambiguous regions
- Example: Temperature that's both "slightly cold" and "slightly comfortable"

### Why Complement?

**Use Case**: "Is it NOT comfortable?"
- Finds opposite of a set
- Useful for negation in rules
- Example: "If NOT comfortable, then adjust temperature"

### Why Algebraic Product?

**Use Case**: More restrictive AND operation
- Stricter than intersection
- Used in some inference systems
- Example: When you need stronger evidence (both conditions must be very true)

### Why Algebraic Sum?

**Use Case**: More inclusive OR operation
- Less restrictive than union
- Used in some inference systems
- Example: When you want to combine evidence

### Why Difference?

**Use Case**: "How much more A than B?"
- Finds elements more in A than B
- Useful for comparisons
- Example: "How much hotter than comfortable?"

---

# PART 2: ASSIGNMENT 2 - FUZZY RELATIONS AND INFERENCE

## 1. Introduction to Fuzzy Inference

### What is Fuzzy Inference?

**Fuzzy Inference** is the process of drawing conclusions from fuzzy rules using fuzzy logic. It's like human reasoning: "If temperature is hot AND humidity is high, THEN comfort is low."

### Components of a Fuzzy Inference System

1. **Fuzzification**: Convert crisp inputs to fuzzy sets
2. **Rule Evaluation**: Apply fuzzy rules
3. **Aggregation**: Combine rule outputs
4. **Defuzzification**: Convert fuzzy output to crisp value

### Why Fuzzy Inference?

- **Handles Uncertainty**: Works with vague, imprecise information
- **Human-like Reasoning**: Mimics how humans make decisions
- **Robust**: Works even with incomplete or noisy data
- **Intuitive**: Rules are easy to understand and modify

---

## 2. Fuzzy Rules

### What is a Fuzzy Rule?

A **Fuzzy Rule** is an IF-THEN statement that relates input fuzzy sets to output fuzzy sets.

**General Form:**
```
IF (condition1) AND/OR (condition2) THEN (conclusion)
```

### Example Rules

**Rule 1**: IF temperature is cold AND humidity is dry THEN comfort is uncomfortable

**Rule 2**: IF temperature is comfortable AND humidity is comfortable THEN comfort is comfortable

### Rule Structure in Assignment 2

The system uses **9 rules** covering all combinations:

| Rule | Temperature | Humidity | Output |
|------|-------------|----------|--------|
| 1 | Cold | Dry | Uncomfortable |
| 2 | Cold | Comfortable | Neutral |
| 3 | Cold | Humid | Uncomfortable |
| 4 | Comfortable | Dry | Neutral |
| 5 | Comfortable | Comfortable | Comfortable |
| 6 | Comfortable | Humid | Neutral |
| 7 | Hot | Dry | Uncomfortable |
| 8 | Hot | Comfortable | Neutral |
| 9 | Hot | Humid | Uncomfortable |

**Why 9 rules?**
- 3 temperature categories × 3 humidity categories = 9 combinations
- Covers all possible input combinations
- Ensures system always has an output

---

## 3. Mamdani Inference System

### What is Mamdani Inference?

**Mamdani Inference** is a popular fuzzy inference method that:
1. Uses **MIN** for AND operations
2. Uses **MAX** for OR operations
3. Produces fuzzy output sets
4. Requires defuzzification

### Step-by-Step Process

#### Step 1: Fuzzification

Convert crisp inputs to membership values.

**Example:**
- Input: Temperature = 25°C, Humidity = 56%
- Calculate memberships:
  - Cold(25°C) = 0.0
  - Comfortable(25°C) = 1.0
  - Hot(25°C) = 0.1353
  - Dry(56%) = 0.0
  - Comfortable(56%) = 1.0
  - Humid(56%) = 0.1645

#### Step 2: Rule Evaluation

For each rule, calculate the **firing strength** (how much the rule applies).

**Formula for Rule with AND:**
```
Firing Strength = MIN(μ_condition1, μ_condition2)
```

**Example - Rule 5:**
```
IF temp=comfortable AND humidity=comfortable THEN comfortable
Firing Strength = MIN(1.0, 1.0) = 1.0
```

**Example - Rule 6:**
```
IF temp=comfortable AND humidity=humid THEN neutral
Firing Strength = MIN(1.0, 0.1645) = 0.1645
```

**Why MIN for AND?**
- Both conditions must be true
- Take the **weaker** membership (more conservative)
- If one is 0.3 and other is 0.8, rule fires with 0.3

#### Step 3: Rule Aggregation

Combine rules that have the same output.

**Formula:**
```
Output Membership = MAX(all rule firing strengths for that output)
```

**Example:**
- Uncomfortable rules: Rule1, Rule3, Rule7, Rule9
- Rule1 = 0.0, Rule3 = 0.0, Rule7 = 0.0, Rule9 = 0.1353
- Uncomfortable = MAX(0.0, 0.0, 0.0, 0.1353) = **0.1353**

**Why MAX for OR?**
- Any rule can contribute
- Take the **stronger** membership
- If Rule1=0.2 and Rule3=0.5, output is 0.5

#### Step 4: Defuzzification

Convert fuzzy output to crisp value.

**Centroid Method:**
```
Crisp Output = Σ(μ_i × centroid_i) / Σ(μ_i)
```

**Example:**
- Uncomfortable: μ=0.1353, centroid=25
- Neutral: μ=0.1645, centroid=50
- Comfortable: μ=1.0, centroid=75
- Output = (0.1353×25 + 0.1645×50 + 1.0×75) / (0.1353+0.1645+1.0)
- Output = (3.38 + 8.23 + 75) / 1.2998 = **66.63%**

---

## 4. Rule Aggregation

### What is Aggregation?

**Aggregation** combines multiple rules that produce the same output into a single fuzzy set.

### Why Aggregate?

- Multiple rules can fire for the same output
- Need to combine their contributions
- Creates a single fuzzy set for each output category

### Aggregation Methods

**MAX (Used in Assignment 2):**
- Takes maximum membership value
- Simple and commonly used
- Preserves the strongest evidence

**SUM:**
- Adds membership values
- Can exceed 1 (needs normalization)
- More inclusive

**AVERAGE:**
- Takes average of membership values
- Smooths out differences
- Less common

### Example Aggregation

**Rules for "Uncomfortable":**
- Rule 1: IF cold AND dry → 0.0
- Rule 3: IF cold AND humid → 0.0
- Rule 7: IF hot AND dry → 0.0
- Rule 9: IF hot AND humid → 0.1353

**Aggregated:**
```
Uncomfortable = MAX(0.0, 0.0, 0.0, 0.1353) = 0.1353
```

**Visual:**
```
Rule 1:  ────
Rule 3:  ────
Rule 7:  ────
Rule 9:  ────┐
             │
Aggregated:  └─── (takes maximum)
```

---

## 5. Defuzzification

### What is Defuzzification?

**Defuzzification** converts a fuzzy output set into a single crisp (numerical) value.

### Why Defuzzify?

- Real-world systems need crisp outputs
- Example: "Set AC to 66.63%" → needs a number, not "somewhat comfortable"
- Enables action: turn on/off, set speed, etc.

### Defuzzification Methods

#### 1. Centroid Method (Used in Assignment 2)

**Formula:**
```
Output = Σ(μ_i × centroid_i) / Σ(μ_i)
```

**How it works:**
1. Find centroid (center) of each output fuzzy set
2. Weight each centroid by its membership value
3. Calculate weighted average

**Example:**
- Uncomfortable: μ=0.1353, centroid=25
- Neutral: μ=0.1645, centroid=50
- Comfortable: μ=1.0, centroid=75

**Calculation:**
```
Numerator = 0.1353×25 + 0.1645×50 + 1.0×75
          = 3.38 + 8.23 + 75 = 86.61

Denominator = 0.1353 + 0.1645 + 1.0 = 1.2998

Output = 86.61 / 1.2998 = 66.63%
```

**Why Centroid?**
- Most common method
- Smooth and continuous
- Represents "center of mass" of fuzzy set

#### 2. Other Methods (Not in Assignment 2)

**Max Membership (Height Method):**
- Takes value with highest membership
- Simple but can be discontinuous

**Mean of Maximum:**
- Takes average of values with maximum membership
- Good for flat-topped sets

**Weighted Average:**
- Similar to centroid but uses fixed weights
- Faster but less accurate

---

## 6. Code Walkthrough - Assignment 2

### Step-by-Step Execution

#### Step 1: Create Membership Functions

```java
// Input membership functions
TriangularMF coldTemp = new TriangularMF(10, 10, 25);
TrapezoidalMF comfortableTemp = new TrapezoidalMF(15, 20, 25, 30);
GaussianMF hotTemp = new GaussianMF(35, 5, 25, 40);

// Output membership functions
TriangularMF uncomfortable = new TriangularMF(0, 0, 50);
TrapezoidalMF neutral = new TrapezoidalMF(30, 40, 60, 70);
GaussianMF comfortable = new GaussianMF(75, 10, 50, 100);
```

**What happens:**
- Creates 3 input MFs for temperature (different types!)
- Creates 3 input MFs for humidity
- Creates 3 output MFs for comfort level
- Uses different MF types to demonstrate variety

**Why different types?**
- Shows flexibility of fuzzy systems
- Different shapes for different concepts
- Triangular: simple, sharp boundaries
- Trapezoidal: flat top (plateau)
- Gaussian: smooth, natural curve

#### Step 2: Get User Input

```java
double temperature = scanner.nextDouble();
double humidity = scanner.nextDouble();
```

**What happens:**
- User enters crisp values
- Example: 25°C, 56%

#### Step 3: Fuzzification

```java
double coldMem = coldTemp.getMembership(temperature);
double comfTempMem = comfortableTemp.getMembership(temperature);
double hotMem = hotTemp.getMembership(temperature);
```

**What happens:**
- Converts crisp inputs to membership values
- Example at 25°C:
  - Cold = 0.0
  - Comfortable = 1.0
  - Hot = 0.1353

#### Step 4: Rule Evaluation

```java
double rule1 = Math.min(coldMem, dryMem);
double rule2 = Math.min(coldMem, comfHumMem);
// ... etc for all 9 rules
```

**What happens:**
- Calculates firing strength for each rule
- Uses MIN for AND operation
- Example:
  - Rule 5: MIN(1.0, 1.0) = 1.0
  - Rule 6: MIN(1.0, 0.1645) = 0.1645

**Why MIN?**
- Mamdani inference standard
- Conservative approach
- Both conditions must be satisfied

#### Step 5: Rule Aggregation

```java
double uncomfortableLevel = Math.max(rule1, Math.max(rule3, Math.max(rule7, rule9)));
double neutralLevel = Math.max(rule2, Math.max(rule4, Math.max(rule6, rule8)));
double comfortableLevel = rule5;
```

**What happens:**
- Combines rules with same output
- Uses MAX for OR operation
- Example:
  - Uncomfortable = MAX(0.0, 0.0, 0.0, 0.1353) = 0.1353
  - Neutral = MAX(0.0, 0.0, 0.1645, 0.1353) = 0.1645
  - Comfortable = 1.0

**Why MAX?**
- Any contributing rule is valid
- Takes strongest evidence
- Standard Mamdani approach

#### Step 6: Defuzzification

```java
double comfortLevel = defuzzify(uncomfortableLevel, neutralLevel, comfortableLevel,
                               uncomfortable, neutral, comfortable);
```

**What happens:**
- Calculates centroids of output fuzzy sets
- Computes weighted average
- Returns crisp value (e.g., 66.63%)

**Inside defuzzify():**
```java
double uncomfCentroid = (uncomfMF.getLowerBound() + uncomfMF.getUpperBound()) / 2.0;
// For TriangularMF(0, 0, 50): centroid = (0 + 50) / 2 = 25

double numerator = uncomfLevel * uncomfCentroid + 
                  neutralLevel * neutralCentroid + 
                  comfLevel * comfCentroid;

double denominator = uncomfLevel + neutralLevel + comfLevel;

return numerator / denominator;
```

#### Step 7: Interpretation

```java
if (comfortLevel < 30) {
    System.out.println("The environment is UNCOMFORTABLE");
} else if (comfortLevel < 70) {
    System.out.println("The environment is NEUTRAL");
} else {
    System.out.println("The environment is COMFORTABLE");
}
```

**What happens:**
- Converts numerical output to linguistic term
- Provides human-readable result
- Example: 66.63% → "NEUTRAL"

---

## 7. Complete Example Walkthrough

### Input
- Temperature: **25°C**
- Humidity: **56%**

### Step 1: Fuzzification

**Temperature (25°C):**
- Cold: Triangular(10,10,25) → μ = 0.0
- Comfortable: Trapezoidal(15,20,25,30) → μ = 1.0
- Hot: Gaussian(35,5) → μ = 0.1353

**Humidity (56%):**
- Dry: Triangular(0,0,50) → μ = 0.0
- Comfortable: Trapezoidal(30,40,60,70) → μ = 1.0
- Humid: Gaussian(75,10) → μ = 0.1645

### Step 2: Rule Evaluation

| Rule | Condition | Calculation | Result |
|------|-----------|-------------|--------|
| 1 | Cold AND Dry | MIN(0.0, 0.0) | 0.0000 |
| 2 | Cold AND Comfortable | MIN(0.0, 1.0) | 0.0000 |
| 3 | Cold AND Humid | MIN(0.0, 0.1645) | 0.0000 |
| 4 | Comfortable AND Dry | MIN(1.0, 0.0) | 0.0000 |
| 5 | Comfortable AND Comfortable | MIN(1.0, 1.0) | **1.0000** |
| 6 | Comfortable AND Humid | MIN(1.0, 0.1645) | 0.1645 |
| 7 | Hot AND Dry | MIN(0.1353, 0.0) | 0.0000 |
| 8 | Hot AND Comfortable | MIN(0.1353, 1.0) | 0.1353 |
| 9 | Hot AND Humid | MIN(0.1353, 0.1645) | 0.1353 |

### Step 3: Aggregation

**Uncomfortable:**
- Rules: 1, 3, 7, 9
- Values: 0.0, 0.0, 0.0, 0.1353
- Result: MAX(0.0, 0.0, 0.0, 0.1353) = **0.1353**

**Neutral:**
- Rules: 2, 4, 6, 8
- Values: 0.0, 0.0, 0.1645, 0.1353
- Result: MAX(0.0, 0.0, 0.1645, 0.1353) = **0.1645**

**Comfortable:**
- Rules: 5
- Value: 1.0
- Result: **1.0000**

### Step 4: Defuzzification

**Calculate Centroids:**
- Uncomfortable: Triangular(0,0,50) → centroid = 25
- Neutral: Trapezoidal(30,40,60,70) → centroid = 50
- Comfortable: Gaussian(75,10) → centroid = 75

**Weighted Average:**
```
Numerator = 0.1353×25 + 0.1645×50 + 1.0×75
          = 3.38 + 8.23 + 75
          = 86.61

Denominator = 0.1353 + 0.1645 + 1.0
            = 1.2998

Output = 86.61 / 1.2998 = 66.63%
```

### Step 5: Interpretation

- Output: 66.63%
- Since 30 ≤ 66.63 < 70
- Result: **"The environment is NEUTRAL"**

---

## Key Concepts Summary

### Assignment 1 - Fuzzy Set Operations

1. **Fuzzy Sets**: Allow partial membership (0 to 1)
2. **Membership Functions**: Map values to membership degrees
3. **Universe of Discourse**: Range of all possible values
4. **Operations**: Union (MAX), Intersection (MIN), Complement (1-x), etc.
5. **Purpose**: Manipulate and combine fuzzy sets

### Assignment 2 - Fuzzy Inference

1. **Fuzzy Rules**: IF-THEN statements with fuzzy conditions
2. **Fuzzification**: Convert crisp inputs to fuzzy
3. **Rule Evaluation**: Calculate how much each rule applies (MIN for AND)
4. **Aggregation**: Combine rules (MAX for OR)
5. **Defuzzification**: Convert fuzzy output to crisp value (Centroid method)
6. **Purpose**: Make decisions based on fuzzy rules

---

## Presentation Tips for Professor

### What to Emphasize

1. **Theory Understanding**:
   - Explain why fuzzy logic is needed
   - Show understanding of membership functions
   - Demonstrate knowledge of operations

2. **Implementation Details**:
   - Walk through code step-by-step
   - Explain why each operation is used
   - Show how formulas are implemented

3. **Practical Application**:
   - Give real-world examples
   - Show how system handles different inputs
   - Demonstrate robustness

### Common Questions & Answers

**Q: Why use MIN for AND and MAX for OR?**
A: MIN ensures both conditions must be satisfied (conservative), MAX allows any condition to contribute (inclusive). This matches logical AND/OR behavior.

**Q: Why defuzzify?**
A: Real systems need crisp outputs. Defuzzification converts fuzzy conclusions into actionable numbers.

**Q: What if multiple rules fire?**
A: We aggregate them using MAX, taking the strongest evidence. This is standard Mamdani inference.

**Q: Why different membership function types?**
A: Different shapes model different concepts better. Triangular is simple, Gaussian is smooth, Trapezoidal has a plateau.

---

## Practice Examples

### Example 1: Temperature = 20°C

**Fuzzification:**
- Cold(20) = 0.333
- Comfortable(20) = 0.333
- Hot(20) = 0.0

**Operations (Assignment 1):**
- Union(Cold, Hot) = MAX(0.333, 0.0) = 0.333
- Intersection(Cold, Comfortable) = MIN(0.333, 0.333) = 0.333

### Example 2: Temperature = 30°C, Humidity = 80%

**Fuzzification:**
- Hot(30) = 0.6065
- Humid(80) = 0.6065

**Rule Evaluation:**
- Rule 9: MIN(0.6065, 0.6065) = 0.6065

**Expected Output:**
- Higher "uncomfortable" level
- Lower comfort percentage

---

This documentation covers all concepts, theory, implementation details, and presentation preparation for both assignments. Study each section thoroughly and practice explaining the concepts in your own words!

