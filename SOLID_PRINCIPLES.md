# SOLID Principles Assessment - Dibs Project

This document evaluates the Dibs codebase against the five SOLID software design principles and recommends architectural changes to improve decoupling, maintainability, and testing.

---

## SOLID Principles Matrix

| Principle | Status | Summary of Findings |
| :--- | :--- | :--- |
| **S**ingle Responsibility Principle | Partially Violated | Controllers handle cookie and raw token orchestration; services mix storage mapping with business logic. |
| **O**pen/Closed Principle | Partially Violated | Extending security handlers or introducing new login channels requires direct modifications to security config classes. |
| **L**iskov Substitution Principle | Followed (with limitations) | `userPrincipal` implements `UserDetails` cleanly but hardcodes authorization permissions. |
| **I**nterface Segregation Principle | Followed | Spring Data repository layers leverage clean segregation; no wide, custom interfaces force redundant operations. |
| **D**ependency Inversion Principle | Violated | Controllers and services autowire concrete classes directly rather than relying on service abstractions. |

---

## Detailed Evaluation & Recommendations

### 1. Single Responsibility Principle (SRP)

*A class should have one, and only one, reason to change.*

*   **Findings**:
    *   **Controller Overreach**: The [userController](file:///c:/Projects/spring/Dibs/src/main/java/org/me/dibs/controller/userController.java) handles authentication routing, but its `login` method also orchestrates raw token generation via `jwtService`, builds HTTP-only cookies, configures browser flags (SameSite, MaxAge), and writes headers directly.
    *   **Service Storage Details**: [ItemService](file:///c:/Projects/spring/Dibs/src/main/java/org/me/dibs/service/ItemService.java) extracts raw bytes from a `MultipartFile` (`image.getBytes()`) while persisting item metadata.
*   **Recommended Changes**:
    *   Extract login cookie construction to an `AuthenticationOrchestrator` or `CookieUtility` class.
    *   Decouple file storage and conversion from business logic by introducing an `ImageStorageService` interface to handle multipart image uploads and storage.

---

### 2. Open/Closed Principle (OCP)

*Software entities (classes, modules, functions) should be open for extension, but closed for modification.*

*   **Findings**:
    *   **Security Configuration**: [SecurityConfig](file:///c:/Projects/spring/Dibs/src/main/java/org/me/dibs/config/SecurityConfig.java) defines inline security rules, CORS controls, and authentication handling. Introducing new identity providers or security policies requires modifying the config class directly.
    *   **Token Parsing**: [JwtFilter](file:///c:/Projects/spring/Dibs/src/main/java/org/me/dibs/config/JwtFilter.java) hardcodes cookie reading logic. If the token location changes (e.g. to a Custom Header), the filter class must be modified.
*   **Recommended Changes**:
    *   Introduce a `TokenExtractor` interface (e.g., `CookieTokenExtractor`, `HeaderTokenExtractor`) to allow dynamic swapping of token reading strategies without modifying the filter.
    *   Externalize CORS configuration and public endpoint lists to configuration properties.

---

### 3. Liskov Substitution Principle (LSP)

*Subtypes must be substitutable for their base types.*

*   **Findings**:
    *   **User Authorization**: The [userPrincipal](file:///c:/Projects/spring/Dibs/src/main/java/org/me/dibs/controller/userPrincipal.java) class implements `UserDetails` correctly. However, `getAuthorities()` hardcodes a single authority (`Collections.singleton(new SimpleGrantedAuthority("USER"))`).
    *   If the security system is extended to include other roles (such as "ADMIN" or "MODERATOR"), the substitution of `userPrincipal` will fail to accurately reflect permission models.
*   **Recommended Changes**:
    *   Add a roles collection to the [User](file:///c:/Projects/spring/Dibs/src/main/java/org/me/dibs/model/User.java) model, and map it dynamically in `userPrincipal.getAuthorities()`.

---

### 4. Interface Segregation Principle (ISP)

*Clients should not be forced to depend on methods they do not use.*

*   **Findings**:
    *   **Repository Layers**: Repositories (like [userRepository](file:///c:/Projects/spring/Dibs/src/main/java/org/me/dibs/Repository/userRepository.java) or [UserDetailRepository](file:///c:/Projects/spring/Dibs/src/main/java/org/me/dibs/Repository/UserDetailRepository.java)) extend Spring Data's `JpaRepository`. Although `JpaRepository` exposes a wide interface, it is standard practice in Spring, and clients only consume the operations they need. No custom fat interfaces are present.
*   **Recommended Changes**:
    *   None. The current repository abstraction layer respects ISP.

---

### 5. Dependency Inversion Principle (DIP)

*High-level modules should not depend on low-level modules. Both should depend on abstractions.*

*   **Findings**:
    *   **Direct Concrete Dependencies**: Controllers depend directly on concrete service implementations rather than interfaces. For example, [ItemController](file:///c:/Projects/spring/Dibs/src/main/java/org/me/dibs/controller/ItemController.java) autowires the concrete class `ItemService`.
    *   **Service-to-Service Coupling**: [ItemService](file:///c:/Projects/spring/Dibs/src/main/java/org/me/dibs/service/ItemService.java) directly autowires the concrete class `userService`. This makes it difficult to mock dependencies in isolation or swap implementations (e.g., implementing cache-aside strategies).
*   **Recommended Changes**:
    *   Create service interfaces (e.g., `IItemService`, `IUserService`, `IJwtService`).
    *   Implement these interfaces in concrete classes and autowire the interface types in all controllers and services.

---

## Secondary Observation: Code Naming Conventions

*   **Findings**:
    *   Several Java classes begin with lowercase letters (e.g., `userService`, `userController`, `userRepository`, `userPrincipal`).
    *   Standard Java conventions recommend using PascalCase (e.g., `UserService`, `UserController`, `UserRepository`, `UserPrincipal`) to prevent syntax confusion and align with common frameworks.
*   **Recommended Changes**:
    *   Rename lowercase files and class definitions to PascalCase.
