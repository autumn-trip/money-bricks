import {createBrowserRouter} from "react-router-dom";
import {lazy, Suspense} from "react";
import comparisonDepositRouter from "../../comparisonDeposit/router/comparisonDepositRouter";
import DictionaryPage from "../../dictionary/pages/DictionaryPage";
import MoneynewsPage from "../../moneyNews/page/MoneynewsPage";
import loginRouter from "../../member/router/loginRouter";
import memberRouter from "../../member/router/memberRouter";
import accountRouter from "../../member/router/accountRouter";


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
    },
    {
        path: "/moneynews",
        element: (
            <Suspense fallback={Loading}>
                <MoneynewsPage/>
            </Suspense>
        )
    },
    {
        path: "/auth",
        children: loginRouter(),
    },
    {
        path: "/member",
        children: memberRouter(),
    },
    {
        path: "/account",
        children: accountRouter(),
    }
]);

export default root;