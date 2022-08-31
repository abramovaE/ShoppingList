package ru.kotofeya.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.kotofeya.shoppinglist.domain.ShopItem
import ru.kotofeya.shoppinglist.domain.ShopItem.Companion.UNDEFINED_ID
import ru.kotofeya.shoppinglist.domain.ShopListRepository

object ShopListRepositoryImpl : ShopListRepository {

    private val shopListLiveData = MutableLiveData<List<ShopItem>>()
    private val shopList = sortedSetOf<ShopItem>({o1, o2 -> o1.id.compareTo(o2.id)})
    private var autoincrementId = 0

    override fun addShopItem(shopItem: ShopItem) {
        if(shopItem.id == UNDEFINED_ID) {
            shopItem.id = autoincrementId++
        }
        shopList.add(shopItem)
        updateList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldItem = getShopItem(shopItem.id)
        shopList.remove(oldItem)
        addShopItem(shopItem)
    }

    override fun getShopItem(id: Int): ShopItem {
        return shopList.find { it.id == id } ?: throw RuntimeException("Element with id $id not found")

    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLiveData
    }

    private fun updateList(){
        shopListLiveData.value = shopList.toList()
    }
}