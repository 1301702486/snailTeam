package com.team.snail.child.repository;

import com.team.snail.child.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Author: 郭瑞景
 * Date: 2019/6/27
 * Description: No Description
 */
public interface AddressRepository extends JpaRepository<Address, Integer> {
    Address findAddressById(Integer id);
    List<Address> findAllByProvince(String province);
    List<Address> findAllByCity(String city);
    List<Address> findAllByProvinceAndCity(String province, String city);
    List<Address> findAllByProvinceAndCityAndCounty(String province, String city, String county);
    List<Address> findAllByProvinceAndCityAndCountyAndStreet(String province, String city, String county, String street);


}
