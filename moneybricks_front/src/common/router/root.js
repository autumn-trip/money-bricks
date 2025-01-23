import {createBrowserRouter} from "react-router-dom";
import {lazy, Suspense} from "react";
import comparisonDepositRouter from "../../comparisonDeposit/router/comparisonDepositRouter";


const Loading = <div>Loading</div>

const Main = lazy(() => import("../pages/MainPage"));

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
    }
]);

export default root;