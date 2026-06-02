package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 添加地址
     * @param addressBook
     */
    @Override
    public void save(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.save(addressBook);
    }

    /**
     * 查询当前登录用户的所有地址信息
     * @return
     */
    @Override
    public List<AddressBook> list(AddressBook addressBook) {
        return addressBookMapper.list(addressBook);

    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @Override
    public AddressBook getById(Long id) {
        return addressBookMapper.getById(id);
    }

    /**
     * 根据id修改地址
     * @param addressBook
     */
    @Override
    public void update(AddressBook addressBook) {

        addressBookMapper.update(addressBook);
    }

    /**
     *
     * @param addressBook
     */
    @Override
    public void setDefault(AddressBook addressBook) {
        addressBook.setIsDefault(0);
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.updateIsDefaultByUserId(addressBook);

        //2、将当前地址改为默认地址 update address_book set is_default = ? where id = ?
        addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);

    }

    /**
     * 查询默认地址
     * @return
     */
    @Override
    public AddressBook getDefault() {
        AddressBook addressBook = AddressBook.builder().isDefault(1).build();
        List<AddressBook> addressBookList = addressBookMapper.list(addressBook);

        return addressBookList.get(0);
    }

    /**
     * 根据id删除地址
     * @param id
     */
    @Override
    public void delete(Long id) {
        addressBookMapper.delete(id);
    }
}
