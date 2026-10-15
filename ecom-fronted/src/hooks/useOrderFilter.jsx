import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useSearchParams } from "react-router-dom";
import {
    getOrdersForDashboard,
    getUserOrders,
} from "../store/actions";

const useOrderFilter = () => {
    const [searchParams] = useSearchParams();
    const dispatch = useDispatch();

    const { user } = useSelector((state) => state.auth);

    const isAdmin =
        user?.roles?.includes("ROLE_ADMIN");

    const isSeller =
        user?.roles?.includes("ROLE_SELLER");

    useEffect(() => {

        if (isAdmin || isSeller) {

            const params = new URLSearchParams();

            const currentPage = searchParams.get("page")
                ? Number(searchParams.get("page"))
                : 1;

            params.set("pageNumber", currentPage - 1);

            const queryString = params.toString();

            console.log(
                "ADMIN/SELLER QUERY STRING:",
                queryString
            );

            dispatch(
                getOrdersForDashboard(
                    queryString,
                    isAdmin
                )
            );

        } else {

            console.log("Fetching USER orders");

            dispatch(getUserOrders());

        }

    }, [
        dispatch,
        searchParams,
        isAdmin,
        isSeller
    ]);
};

export default useOrderFilter;