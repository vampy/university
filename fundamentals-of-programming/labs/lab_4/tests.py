#!/usr/bin/python

import utils


def test_convert_to_int():
    assert utils.convert_to_int("23") == 23
    assert utils.convert_to_int("0") == 0
    assert utils.convert_to_int("-23") == -23
    assert utils.convert_to_int(245) == 245
    assert utils.convert_to_int(-245) == -245
    assert utils.convert_to_int(0) == 0
    assert utils.convert_to_int("43.53") is None
    assert utils.convert_to_int("25sn") is None
    assert utils.convert_to_int("s45") is None
    assert utils.convert_to_int("string") is None


def test_convert_to_float():
    assert utils.convert_to_float("23.45") == 23.45
    assert utils.convert_to_float("0") == 0.0
    assert utils.convert_to_float("-0") == 0.0
    assert utils.convert_to_float("-23") == -23.0
    assert utils.convert_to_float(245) == 245.0
    assert utils.convert_to_float(-245) == -245.0
    assert utils.convert_to_float(-245.8457) == -245.8457
    assert utils.convert_to_float(0) == 0.0
    assert utils.convert_to_float("43.53") == 43.53
    assert utils.convert_to_float("25sn") is None
    assert utils.convert_to_float("s45") is None
    assert utils.convert_to_float("string") is None


from apartment import Apartment


def test_apartment_init():
    apart = Apartment(water=34, heating=23)

    assert apart.expenses["water"] == 34
    assert apart.expenses["heating"] == 23
    assert apart.expenses["others"] == 0
    assert not apart.expenses["gas"] == 23

    apart2 = Apartment()
    assert sum(apart2.expenses.values()) == 0


def test_apartment_is_expense_type():
    assert Apartment.is_expense_type("gas")
    assert not Apartment.is_expense_type("gas1")
    assert not Apartment.is_expense_type("heating2")
    assert Apartment.is_expense_type("others")
    assert not Apartment.is_expense_type("other")
    assert not Apartment.is_expense_type([])
    assert not Apartment.is_expense_type(23)
    assert not Apartment.is_expense_type(["gas"])
    assert Apartment.is_expense_type("illuminating")


def test_apartment_get_total_expenses():
    water = 34
    others = 3452
    apart = Apartment(water=water, others=others)
    assert apart.get_total_expenses() == (water + others)
    heating = 245
    apart.expenses["heating"] = heating
    assert apart.get_total_expenses() == (water + others + heating)


def test_get_max_expenses_type():
    apart = Apartment(water=100, gas=200)
    assert apart.get_max_expenses_type() == ["gas"]
    apart.expenses["water"] = 200
    apart.expenses["others"] = 200
    assert sorted(apart.get_max_expenses_type()) == sorted(["gas", "water", "others"])
    apart.expenses["heating"] = 999
    apart.expenses["illuminating"] = 23
    assert apart.get_max_expenses_type() == ["heating"]
    apart = Apartment()
    assert apart.get_max_expenses_type() == []
    apart.expenses["heating"] = -233
    assert apart.get_max_expenses_type() == []

import bloc
import ui


def test_bloc():
    bloc_obj = bloc.Bloc()
    #try:
    assert not bloc_obj._command
    assert not bloc_obj._parsed_command

    assert not bloc_obj.insert_apartment_parse("insert")
    assert not bloc_obj.insert_apartment_parse("insert gas")
    assert not bloc_obj.insert_apartment_parse("insert gas, 100 at 2")
    assert not bloc_obj.insert_apartment_parse("insert 100, gas")
    assert not bloc_obj.insert_apartment_parse("insert 233, gass1")
    assert not bloc_obj.insert_apartment_parse("insert 200, heating at")
    assert not bloc_obj.insert_apartment_parse("insert 200, 2 at heating")
    assert not bloc_obj.insert_apartment_parse("insert 200, others at id")
    assert bloc_obj.insert_apartment_parse("insert 200, gas at 1")
    bloc_obj.insert_apartment()

    assert not bloc_obj.replace_apartment_parse("replace")
    assert not bloc_obj.replace_apartment_parse("replace gas")
    assert not bloc_obj.replace_apartment_parse("replace gas, 100 at 2")
    assert not bloc_obj.replace_apartment_parse("replace 2, gas with 100")
    assert bloc_obj.replace_apartment_parse("replace 0, gas at 1")
    bloc_obj.insert_apartment()
    #assert bloc.insert_apartment_parse("replace 200, others at 4")

    assert not bloc_obj.remove_apartment_parse("remove 54")
    assert not bloc_obj.remove_apartment_parse("remove from34")
    assert not bloc_obj.remove_apartment_parse("remove greater than 34")
    assert bloc_obj.remove_apartment_parse("remove from 0 to 0")
    assert bloc_obj.remove_apartment_parse("remove gas")
    assert bloc_obj.remove_apartment_parse("remove from 1 to 100")
    assert not bloc_obj.remove_apartment_parse("remove ")
    assert not bloc_obj.remove_apartment_parse("remove 200, others at id")

    assert not bloc_obj.list_by_type_parse("list gas2")
    assert bloc_obj.list_by_type_parse("list heating")
    assert not bloc_obj.list_by_type_parse("list ilumintaing")
    assert not bloc_obj.list_by_type_parse("list other")

    assert not bloc_obj.list_total_apartment_parse("total 45")
    assert not bloc_obj.list_total_apartment_parse("total 1")
    assert bloc_obj.list_total_apartment_parse("list total 1")
    assert bloc_obj.list_total_apartment_parse("list sold 1")
    assert not bloc_obj.list_total_apartment_parse("list sold gas")
    assert not bloc_obj.list_total_apartment_parse("list sold 1gas")
    assert not bloc_obj.list_total_apartment_parse("list sold gas1")

    assert not bloc_obj.list_greater_than_parse("greater than 300")
    assert bloc_obj.list_greater_than_parse("list greater than 300")
    assert bloc_obj.list_greater_than_parse("list greater than -300")
    assert not bloc_obj.list_greater_than_parse("list greater 300")
    assert not bloc_obj.list_greater_than_parse("list 300 greater than")

    assert not bloc_obj.list_less_than_parse("less than 300")
    assert not bloc_obj.list_less_than_parse("list less than 300")
    assert not bloc_obj.list_less_than_parse("list less than -300")
    assert not bloc_obj.list_less_than_parse("list less 300")
    assert not bloc_obj.list_less_than_parse("list 300 less than")
    assert bloc_obj.list_less_than_parse("list less than 200 before 10")
    assert not bloc_obj.list_less_than_parse("list less than 200 before -1")

    assert not bloc_obj.stat_max_apartment_parse("list max")
    assert not bloc_obj.stat_max_apartment_parse("max gas")
    assert not bloc_obj.stat_max_apartment_parse("max gas1")
    assert not bloc_obj.stat_max_apartment_parse("max gas2")
    assert not bloc_obj.stat_max_apartment_parse("max i am a name")
    assert not bloc_obj.stat_max_apartment_parse("max 23")
    assert bloc_obj.stat_max_apartment_parse("max 1")
    assert not bloc_obj.stat_max_apartment_parse("max 12")

    assert not bloc_obj.stat_total_type_parse("sum 12")
    assert not bloc_obj.stat_total_type_parse("sum -12")
    assert not bloc_obj.stat_total_type_parse("sum 12gas")
    assert not bloc_obj.stat_total_type_parse("sum gas12")
    assert bloc_obj.stat_total_type_parse("sum gas")
    assert bloc_obj.stat_total_type_parse("sum heating")
    assert not bloc_obj.stat_total_type_parse("sum iluminating")

    assert not bloc_obj.sort_parse("sort ascending")
    assert not bloc_obj.sort_parse("sort ascending gas")
    assert not bloc_obj.sort_parse("sort descending")
    assert not bloc_obj.sort_parse("sort descending gas")
    assert not bloc_obj.sort_parse("sort desce gas")
    assert not bloc_obj.sort_parse("sort descen gas")
    assert not bloc_obj.sort_parse("sort gas desc")
    assert not bloc_obj.sort_parse("sort gas asc")
    assert not bloc_obj.sort_parse("sort 2gas desc")
    assert not bloc_obj.sort_parse("sort gas1 asc")
    assert not bloc_obj.sort_parse("sort 1gas asc")
    assert bloc_obj.sort_parse("sort asc")
    assert bloc_obj.sort_parse("sort asc gas")
    assert bloc_obj.sort_parse("sort asc heating")
    assert bloc_obj.sort_parse("sort asc others")
    assert bloc_obj.sort_parse("sort asc water")
    assert bloc_obj.sort_parse("sort desc")
    assert bloc_obj.sort_parse("sort desc gas")
    assert bloc_obj.sort_parse("sort desc water")
    assert bloc_obj.sort_parse("sort desc others")
    assert bloc_obj.sort_parse("sort desc water")
    assert not bloc_obj.sort_parse("sort desc water1")
    assert not bloc_obj.sort_parse("sort asc water1")
    assert not bloc_obj.sort_parse("sort asc gass")
    assert not bloc_obj.sort_parse("sort desc gass")
    assert not bloc_obj.sort_parse("sort desc iluminting")

    assert not bloc_obj.filter_parse("filter")
    assert not bloc_obj.filter_parse("filter value")
    assert bloc_obj.filter_parse("filter 23")
    assert bloc_obj.filter_parse("filter -23")
    assert not bloc_obj.filter_parse("filter gass")
    assert not bloc_obj.filter_parse("filter ilumintaing")
    assert not bloc_obj.filter_parse("filter other")
    assert bloc_obj.filter_parse("filter others")
    assert bloc_obj.filter_parse("filter gas")

    #except AssertionError as e:
    #    print(ui.UI.get_message())
    #    print(e)
    #    raise e

if __name__ == "__main__":
    test_convert_to_int()
    test_convert_to_float()

    test_apartment_init()
    test_apartment_is_expense_type()
    test_apartment_get_total_expenses()
    test_get_max_expenses_type()
    test_bloc()

    print("All tests passed")