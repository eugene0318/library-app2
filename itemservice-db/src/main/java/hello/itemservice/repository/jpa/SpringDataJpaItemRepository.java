package hello.itemservice.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hello.itemservice.domain.Item;

public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {

	// 쿼리 메서드 기능
	List<Item> findByItemNameLike(String itemName);

	// 쿼리 직접 실행
	@Query("select i from Item i where i.itemName like :itemName and i.price <= :price")
	List<Item> findItems(@Param("itemName") String itemName, @Param("price") Integer price);
}
