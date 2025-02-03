import {createBrowserRouter} from "react-router-dom";
import {lazy, Suspense} from "react";
import comparisonDepositRouter from "../../comparisonDeposit/router/comparisonDepositRouter";
import DictionaryPage from "../../dictionary/pages/DictionaryPage";


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
    {
        path: "/dictionary",
        element: (
            <Suspense fallback={Loading}>
                <DictionaryPage/>
            </Suspense>
        )
    }
]);

export default root;