# AGENTS.md  
Guidance for OpenAI Codex when operating on this **Forge ➞ FFTCG** fork.  
Codex reads this file before each task and applies the rules below.

---

## 🛠 Environment
- **Java 17** (OpenJDK) is required.  
  `JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64`  
- Build tool: **Maven** project (multi-module).
- Codex runs in a network-isolated sandbox; no external downloads during tests.

---

## 🏗 Build & Test Commands  
Codex must execute these after every code change.  
```bash
mvn -q test      # compile + unit/integration tests
```

---

## 🚀 Manual Run / Smoke-test (optional)

Use only when a task explicitly says “run the client”.
```bash
mvn -q -pl forge-gui-desktop -am exec:java -Dexec.mainClass=forge.view.Main
```

---

## 📁 Project Notes

| Area             | Path                                    | Purpose                                                            |
| ---------------- | --------------------------------------- | ------------------------------------------------------------------ |
| **Engine core**  | `forge-core/`                           | Game logic, cost system, zones, stack                              |
| **Cost package** | `forge-game/src/main/java/forge/game/cost/`   | `ManaPool` (existing) and **new** `CrystalPool` / `CrystalElement` |
| **Player state** | `forge-game/src/main/java/forge/game/player/` | Add `crystal` field to `GamePlayer`                                |
| **Tests**        | `forge-game/src/test/java/`             | JUnit 5 (`org.junit.jupiter`)                                      |

**Important boundaries**

1. Keep **`ManaPool` and mana logic untouched**; FFTCG additions live alongside them.
2. Presume GPL-3.0: *preserve original headers* when copying or modifying Forge code.
3. UI packages (`forge-gui-desktop`, JavaFX) must not be edited until core CP logic compiles and passes tests.

---

## ✍️ Coding Guidelines

* 4-space indents, no wildcard (`*`) imports.
* Public methods ≤ 50 LOC where practical; split complex logic.
* Javadoc for all public classes/methods.
* Commit messages:

  ```
  <type>(<scope>): <summary>

  <short body – optional>
  ```

  Examples: `feat(cost): add CrystalPool`, `test: cover canPay edge cases`.
* New files must start with:

  ```java
  /*
   * Forge: Magic The Gathering © 2021 - current Forge Team
   * (GPL-3.0 header as in original file)
   * Modifications for Final Fantasy TCG © 2025 <Your Name>
   */
  ```

---

## ⚖️ Domain Constraints

1. **No network I/O** in production or test code.
2. Maintain deterministic unit tests (no timing or random order dependencies).
3. Do **not** introduce non-OSI licences; derivative code remains GPL-3.0.
4. Do **not** remove or weaken existing Forge rule validations.

---

## 🎯 Initial Road-map (for reference)

1. **Crystal data model** – `CrystalElement`, `CrystalPool`, add to `GamePlayer`.
2. **CP generation rules** – +2 CP on discard; “Tap: +1 CP” for Backup permanents.
3. **Cost payment** – extend cost checker to consume CP and allow element mismatch rule.
4. Zones: `FORWARD_ROW`, `BACKUP_ROW`, `DAMAGE`.
5. EX-Burst and sample cards.

Each milestone should be a separate, compilable Codex task.

---

All sections above are authoritative.  Any Codex suggestion conflicting with these rules must be rejected or revised.

