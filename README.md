BIG COMPANY employs many people and wants to analyze its organizational structure.

The board has two main rules to verify:

Manager Salary Rule

Every manager must earn at least 20% more than the average salary of their direct subordinates.

A manager must not earn more than 50% more than that average.

Reporting Line Rule

No employee should have more than 4 managers above them in the reporting chain (from CEO downwards).

The input is provided in a CSV file with the following format:

Id,firstName,lastName,salary,managerId

Id → Employee unique ID.

firstName,lastName → Employee name.

salary → Salary of employee.

managerId → ID of their manager (blank if CEO).

Example Input (employees.csv)
Id,firstName,lastName,salary,managerId
123,Joe,Doe,60000,
124,Martin,Chekov,45000,123
125,Bob,Ronstad,47000,123
300,Alice,Hasacat,50000,124
305,Brett,Hardleaf,34000,300

Hierarchy

CEO: Joe Doe 
├── Martin Chekov 
│     └── Alice Hasacat 
│           └── Brett Hardleaf 
└── Bob Ronstad 


Rule 1 – Salary Check

Case 1: Joe Doe (CEO, 60000)

Direct subs → Martin (45000), Bob (47000).

Avg salary = (45000+47000)/2 = 46000.

Allowed range = 55200 – 69000.

Salary = 60000 (OK)

Case 2: Martin Chekov (45000)

Subordinate → Alice (50000).

Avg = 50000.

Allowed range = 60000 – 75000.

Salary = 45000  Underpaid by 15000.

Case 3: Alice Hasacat (50000)

Subordinate → Brett (34000).

Avg = 34000.

Allowed range = 40800 – 51000.

Salary = 50000  (OK)

Case 4: Bob Ronstad (47000) → No subordinates → Skip.

Case 5: Brett Hardleaf (34000) → No subordinates → Skip.

Salary summary:

Joe Doe →  OK

Martin Chekov →  Underpaid by 15000

Alice Hasacat → = OK

 Rule 2 – Reporting Line Depth

Joe (CEO) → 0 managers above 

Martin → 1 

Bob → 1 

Alice → 2 

Brett → 3 

 All reporting lines ≤ 4 →  No issues.

Expected Program Output

=== Underpaid Managers ===
Martin Chekov (id=124, salary=45000.0) earns less than they should by 15000.00

=== Overpaid Managers ===
No issues in salary, all OK

=== Reporting Line Issues ===
No issues in reporting, all OK


