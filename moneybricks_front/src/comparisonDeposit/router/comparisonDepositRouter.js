import {lazy, Suspense} from "react";

const Loading = <div>Loading...</div>;
const ComparisonDepositList = lazy(() => import("../pages/ComparisonDepositPage"));

const comparisonDepositRouter = [
    {
        path: "list",
        element: (
            <Suspense fallback={Loading}>
                <ComparisonDepositList />
            </Suspense>
        ),
    },
];

export default comparisonDepositRouter;