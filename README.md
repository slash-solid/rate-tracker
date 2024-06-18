## Cross provider rates tracker

### Main idea

Displaying of a final outcome of a chain of conversions across different providers.
```
Bank 1       Bank 2       Bank 3
USD/CAD  ->  CAD/EUR  ->  EUR/UAH
USD ------------------------> UAH
```

Collection of statistics about rates over time to detect most profitable period.

```
ABC -> DEF

                      +- 24.0 (current)
- 22.7 -+            /
         \          /
          +- 21.2 -+

-- 01 ------ 02 --------- 03 ------------>
```