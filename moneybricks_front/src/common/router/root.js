import {createBrowserRouter} from "react-router-dom";
import {lazy, Suspense} from "react";
import comparisonDepositRouter from "../../comparisonDeposit/router/comparisonDepositRouter";


const Loading = <div>Loading</div>

const Main = lazy(() => import("../pages/MainPage"));
const OwnProduct = lazy(() => import("../../ownProduct/pages/OwnProductPage"))

const root = createBrowserRouter([
    {
        path: "/",
        element: (
            <Suspense fallback={Loading}>
                <Main />
            </Suspense>
        )
    },
    {
        path: "/product",
        children: comparisonDepositRouter,
    },
    {
        path: "/ownproduct",
        element: (
            <Suspense fallback={Loading}>
                <OwnProduct />
            </Suspense>
        )
    },
]);

export default root;