# Code Rules

## 메서드 네이밍

**Finder / Manager** 메서드명에 조건절(`ByXxx`)을 붙이지 않는다. 파라미터가 이미 조건을 설명한다.

```java
// ❌
findProductsByCategoryId(Long categoryId)
findCartItemsByUserId(Long userId)

// ✅
findProducts(Long categoryId)
findCartItems(Long userId)
```

### 파라미터 타입 충돌 시

같은 이름으로 오버로딩하려는 두 메서드의 파라미터 타입이 동일하면(`Long, Long` 등) Java가 구분하지 못한다.

1. **Manager 내부로 흡수** (우선) — 두 번째 조회가 write 흐름의 일부라면 외부에 노출하지 않고 Manager 안에서 처리한다.
2. **역할을 명사로 구분** — Finder에 노출이 꼭 필요하다면 `ByXxx` 대신 목적어나 역할을 이름에 추가한다.

```java
// ❌ (Long, Long) 타입 충돌로 오버로딩 불가
findCartItem(Long cartItemId, Long userId)
findCartItem(Long userId, Long productOptionId)

// ✅ 역할을 명사로 구분
findCartItem(Long cartItemId, Long userId)
findCartItemForOption(Long userId, Long productOptionId)
```

## 트랜잭션

트랜잭션은 **Finder / Manager** 클래스에 건다. Service에는 걸지 않는다.

| 클래스 유형 | 어노테이션 |
|------------|-----------|
| `*Finder` | `@Transactional(readOnly = true)` |
| `*Manager` | `@Transactional` |

## 예시

```java
@Transactional(readOnly = true)
@Component
class OrderFinder {
    Order findOrder(Long orderId) { ... }
}

@Transactional
@Component
class OrderManager {
    void save(Order order) { ... }
}
```
