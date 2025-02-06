import { lazy, Suspense } from "react";
import LoadingSpinner from "../../common/component/LoadingSpinner";

const SignUpPage = lazy(() => import("../../member/pages/MemberSignupPage"));
const SignUpSuccessPage = lazy(() => import("../../member/pages/MemberSignupSuccessPage"));
const ModifyPage = lazy(() => import("../../member/pages/MemberModifyPage"));
const ChangePasswordPage = lazy(() => import("../../member/pages/ChangePasswordPage"));
const DeletePage = lazy(() => import("../../member/pages/MemberDeletePage"));

const memberRouter = () => {
	return [
		// {
		//     path: "modify",
		//     element: (
		//         <Suspense fallback={Loading}>
		//             <MemberModify />
		//         </Suspense>
		//     ),
		// },

		{
			path: "sign-up",
			element: (
				<Suspense fallback={<LoadingSpinner isLoading={true} />}> {/* 로딩 스피너 적용 */}
					<SignUpPage />
				</Suspense>
			),
		},
		{
			path: "signup-success",
			element: (
				<Suspense fallback={<LoadingSpinner isLoading={true} />}> {/* 로딩 스피너 적용 */}
					<SignUpSuccessPage />
				</Suspense>
			),
		},
		{
			path: "settings/edit-member",
			element: (
				<Suspense fallback={<LoadingSpinner isLoading={true} />}> {/* 로딩 스피너 적용 */}
					<ModifyPage />
				</Suspense>
			),
		},
		{
			path: "settings/change-password",
			element: (
				<Suspense fallback={<LoadingSpinner isLoading={true} />}> {/* 로딩 스피너 적용 */}
					<ChangePasswordPage />
				</Suspense>
			),
		},
		{
			path: "settings/delete-account",
			element: (
				<Suspense fallback={<LoadingSpinner isLoading={true} />}> {/* 로딩 스피너 적용 */}
					<DeletePage />
				</Suspense>
			),
		},
	];
};

export default memberRouter;
