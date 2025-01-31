import ComparisonDepositComponent from "../component/ComparisonDepositComponent";
import FooterComponent from "../../common/component/FooterComponent";
import BasicMenu from "../../common/pages/BasicMenu";

const ComparisonDepositPage = () => {
    return (
        <div>
            <BasicMenu />
            <div>
                <ComparisonDepositComponent />
            </div>
            <FooterComponent />
        </div>
    )
};

export default ComparisonDepositPage;