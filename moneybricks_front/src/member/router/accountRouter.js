import React, { lazy, Suspense } from "react";
import LoadingSpinner from "../../common/component/LoadingSpinner";

const AccountDetailsPage = lazy(() => import("../../savingsAccount/pages/AccountDetailsPage"));

const accountRouter = () => {
	return [
		{
			path: "",
			element: (
				<Suspense fallback={<LoadingSpinner isLoading={true}/>}> {/* 로딩 스피너 적용 */}
					<AccountDetailsPage />
				</Suspense>
			),
		},
	];
};

export default accountRouter;