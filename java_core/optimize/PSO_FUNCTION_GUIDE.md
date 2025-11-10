# How to Change Objective Function in PSO

## Quick Guide

To change the objective function, you only need to modify **3 lines** in the `main()` method:

### Step 1: Choose the Function (Line ~321)
Change this line:
```java
FitnessFunction objective = sphere();  // Current
```

To one of these:
```java
FitnessFunction objective = rastrigin();
FitnessFunction objective = rosenbrock();
FitnessFunction objective = ackley();
FitnessFunction objective = griewank();
```

### Step 2: Update Bounds (Lines ~330-331)
Change the bounds according to the function:

| Function    | Lower Bound | Upper Bound |
|-------------|-------------|-------------|
| Sphere      | -5.12       | 5.12        |
| Rastrigin   | -5.12       | 5.12        |
| Rosenbrock  | -2.0        | 2.0         |
| Ackley      | -5.0        | 5.0         |
| Griewank    | -600        | 600         |

Example for Rastrigin:
```java
Arrays.fill(lo, -5.12);  // Lower bound
Arrays.fill(hi, 5.12);   // Upper bound
```

### Step 3: Update Function Name for CSV (Line ~342)
Change the filename:
```java
String functionName = "rastrigin";  // Match your function
```

## Complete Example: Switching to Rastrigin

```java
// Line ~321: Change function
FitnessFunction objective = rastrigin();

// Lines ~330-331: Bounds stay the same for Rastrigin
Arrays.fill(lo, -5.12);
Arrays.fill(hi, 5.12);

// Line ~342: Update filename
String functionName = "rastrigin";
```

## Complete Example: Switching to Rosenbrock

```java
// Line ~321: Change function
FitnessFunction objective = rosenbrock();

// Lines ~330-331: Change bounds
Arrays.fill(lo, -2.0);
Arrays.fill(hi, 2.0);

// Line ~342: Update filename
String functionName = "rosenbrock";
```

## Adding Your Own Custom Function

If you want to add a custom function, add it in the benchmark functions section (around line 231):

```java
public static FitnessFunction myCustomFunction() {
    // Your function: f(x) = ...
    return (x) -> {
        double result = 0;
        // Your calculation here
        for (double v : x) {
            // Your formula
        }
        return result;
    };
}
```

Then use it in main():
```java
FitnessFunction objective = myCustomFunction();
```

## Available Functions

1. **Sphere**: `f(x) = sum(x_i^2)`
   - Global minimum: f(0,...,0) = 0
   - Easy, unimodal

2. **Rastrigin**: `f(x) = 10*n + sum(x_i^2 - 10*cos(2*pi*x_i))`
   - Global minimum: f(0,...,0) = 0
   - Highly multimodal, many local minima

3. **Rosenbrock**: `f(x) = sum(100*(x_{i+1} - x_i^2)^2 + (1 - x_i)^2)`
   - Global minimum: f(1,...,1) = 0
   - Narrow valley, challenging

4. **Ackley**: `f(x) = -20*exp(-0.2*sqrt(mean(x^2))) - exp(mean(cos(2*pi*x))) + 20 + e`
   - Global minimum: f(0,...,0) = 0
   - Many local minima, deceptive

5. **Griewank**: `f(x) = 1 + sum(x_i^2/4000) - product(cos(x_i/sqrt(i)))`
   - Global minimum: f(0,...,0) = 0
   - Many local minima, product term makes it complex

