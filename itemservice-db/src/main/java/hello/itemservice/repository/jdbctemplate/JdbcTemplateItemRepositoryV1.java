package hello.itemservice.repository.jdbctemplate;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;
import javax.swing.tree.RowMapper;

import org.h2.expression.Rownum;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;

public class JdbcTemplateItemRepositoryV1 implements ItemRepository {

	private final JdbcTemplate template;

	public JdbcTemplateItemRepositoryV1(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}

	@Override
	public Item save(Item item) {
		String sql = "insert into item(item_name, price, quantity) values (?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		template.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
			ps.setString(1, item.getItemName());
			ps.setInt(2, item.getPrice());
			ps.setInt(3, item.getQuantity());
			return ps;
		}, keyHolder);

		long key = keyHolder.getKey().longValue();
		item.setId(key);
		return item;
	}

	@Override
	public void update(Long itemId, ItemUpdateDto updateParam) {
		String sql = "update item set item_name = ?, price = ?, quantity=? where id = ?";
		template.update(sql, updateParam.getItemName(), updateParam.getPrice(), updateParam.getQuantity(), itemId);
	}

	@Override
	public Optional<Item> findById(Long id) {
		String sql = "select id, item_name, price, quantity where id = ?";

		try {
			Item item = template.queryForObject(sql, itemRowMapper(), id);
			return Optional.of(item);
		} catch (EmptyResultDataAccessException e) {
			// TODO: handle exception
			return Optional.empty();
		}
	}

	private org.springframework.jdbc.core.RowMapper<Item> itemRowMapper() {
		return ((rs, RowNum) -> {
			Item item = new Item();
			item.setId(rs.getLong("id"));
			item.setItemName(rs.getString("item_name"));
			item.setPrice(rs.getInt("price"));
			item.setQuantity(rs.getInt("quantity"));
			return item;
		});
	}

	@Override
	public List<Item> findAll(ItemSearchCond cond) {
		// TODO Auto-generated method stub
		return null;
	}

}
