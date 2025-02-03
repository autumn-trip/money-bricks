import { Suspense, lazy } from "react";
import { createBrowserRouter } from "react-router-dom";
import DictionaryPage from "../pages/DictionaryPage";
import MoneynewsPage from "../pages/moneynews/MoneynewsPage";

const Loading = <div>Loading....</div>;

// const Main = lazy(() => import("../pages/MainPage"));
//

const root = createBrowserRouter([
    {
        path: "/",
        element: (
            <Suspense fallback={Loading}>

            </Suspense>
        ),
    },
    {
        path: "/dictionary",
        element: (
            <Suspense fallback={Loading}>
                <DictionaryPage/>
            </Suspense>
        ),
    },
    {
        path: "/moneynews",
        element: (
            <Suspense fallback={Loading}>
                <MoneynewsPage />
            </Suspense>
        )


    }

]);

export default root;
