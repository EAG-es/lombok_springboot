interface OrderDTO {
    id: string;
    orderNumber: string;
    productId: string;
    quantity: string;
    totalPrice: string;
}

interface PageContent {
    content: OrderDTO[];
    page: {
        totalElements: number;
        totalPages: number;
        number: number;
        size: number;
    };
}

interface ReadOpProps {
    location: string;
    orderDTO: OrderDTO;
    setOrderDTO: (data: OrderDTO) => void;
    onSuccess: (selectedOrder: OrderDTO, searchCriteria: OrderDTO, page: number) => void;
    onError: (error: string) => void;
    onBack: (searchCriteria: OrderDTO, page: number) => void;
    lang: string;
    refreshKey?: number;
    initialPage?: number;
}

const ReadOp: React.FC<ReadOpProps> = ({ location, orderDTO, setOrderDTO, onSuccess, onError, onBack, lang, refreshKey, initialPage = 0 }) => {
    const React = (window as any).React;
    const [loading, setLoading] = React.useState(true);
    const [pageData, setPageData] = React.useState(null as PageContent | null);
    const [currentPage, setCurrentPage] = React.useState(initialPage);
    const [searchCriteria, setSearchCriteria] = React.useState(orderDTO);
    const t = (window as any).i18n[lang] || (window as any).i18n.en;

    const csrfToken = document.querySelector<HTMLMetaElement>('meta[name="_csrf"]')?.content;
    const csrfHeaderName = document.querySelector<HTMLMetaElement>("meta[name='_csrf_header']")?.content;

    const fetchPage = async (page: number, criteria?: OrderDTO) => {
        setLoading(true);
        const searchData = criteria || searchCriteria;
        try {
            const url = new URL('/orders/select', window.location.origin);
            url.searchParams.append('page', page.toString());
            url.searchParams.append('pageSize', '10');

            const response = await fetch(url.toString(), {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Location': location,
                    [csrfHeaderName || 'X-CSRF-TOKEN']: csrfToken || ''
                },
                body: JSON.stringify(searchData)
            });

            if (!response.ok) {
                const errorText = await response.text();
                let errorMsg = `HTTP Error ${response.status}: ${response.statusText}`;
                try {
                    const errorJson = JSON.parse(errorText);
                    errorMsg = errorJson.message || errorMsg;
                } catch (e) {
                    errorMsg = errorText || errorMsg;
                }
                throw new Error(errorMsg);
            }

            const result = await response.json();
            setPageData(result);
            setCurrentPage(page);
            if (criteria) {
                setSearchCriteria(criteria);
            }
        } catch (error: any) {
            onError(error.message);
        } finally {
            setLoading(false);
        }
    };

    React.useEffect(() => {
        fetchPage(initialPage, orderDTO);
    }, [refreshKey]);

    const handlePrevious = () => {
        if (currentPage > 0) {
            fetchPage(currentPage - 1);
        }
    };

    const handleNext = () => {
        if (pageData && currentPage < pageData.page.totalPages - 1) {
            fetchPage(currentPage + 1);
        }
    };

    const handleRowClick = (order: OrderDTO) => {
        const selectedOrder = {
            id: order.id || '',
            orderNumber: order.orderNumber || '',
            productId: order.productId || '',
            quantity: order.quantity || '',
            totalPrice: order.totalPrice || ''
        };
        setOrderDTO(selectedOrder);
        onSuccess(selectedOrder, searchCriteria, currentPage);
    };

    const handleBack = () => {
        onBack(searchCriteria, currentPage);
    };

    const firstRow = pageData ? currentPage * pageData.page.size + 1 : 0;
    const totalRows = pageData ? pageData.page.totalElements : 0;

    if (loading) {
        return (
            <div className="card shadow-lg border-0 mb-5 text-center p-4">
                <div className="spinner-border text-primary mx-auto mb-3" role="status">
                    <span className="visually-hidden">Loading...</span>
                </div>
                <div className="text-muted">{t.labelLoading}</div>
            </div>
        );
    }

    if (!pageData || pageData.content.length === 0) {
        return (
            <div className="card shadow-lg border-0 mb-5 p-4">
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <h5 className="mb-0 text-primary fw-bold"><i className="bi bi-list-ul me-2"></i>Results</h5>
                    <button className="btn btn-outline-secondary btn-sm rounded-pill px-3" onClick={handleBack}>
                        <i className="bi bi-arrow-left me-1"></i>{t.btnBack}
                    </button>
                </div>
                <div className="alert alert-warning border-0 shadow-sm text-center">
                    No orders found matching your criteria.
                </div>
            </div>
        );
    }

    return (
        <div className="card shadow-lg border-0 mb-5 p-4 animate__animated animate__fadeIn">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h5 className="mb-0 text-primary fw-bold"><i className="bi bi-list-ul me-2"></i>Results</h5>
                <button className="btn btn-outline-secondary btn-sm rounded-pill px-3 shadow-sm" onClick={handleBack}>
                    <i className="bi bi-arrow-left me-1"></i>{t.btnBack}
                </button>
            </div>

            <div className="table-responsive">
                <table className="table table-hover align-middle border-top">
                    <thead className="table-light">
                        <tr className="text-muted small text-uppercase">
                            <th className="py-3">ID</th>
                            <th className="py-3">Order Number</th>
                            <th className="py-3">Product ID</th>
                            <th className="py-3">Quantity</th>
                            <th className="py-3">Total Price</th>
                            <th className="py-3 text-end">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        {pageData.content.map((order: OrderDTO) => (
                            <tr key={order.id} className="border-bottom">
                                <td className="fw-medium text-dark py-3">{order.id}</td>
                                <td className="text-secondary">{order.orderNumber}</td>
                                <td className="text-secondary">{order.productId}</td>
                                <td className="text-secondary">{order.quantity}</td>
                                <td className="text-secondary">${order.totalPrice}</td>
                                <td className="text-end">
                                    <button
                                        className="btn btn-sm btn-outline-primary rounded-pill px-3 shadow-sm transition-all"
                                        onClick={() => handleRowClick(order)}
                                        title={t.btnSelect}
                                    >
                                        <i className="bi bi-check2-circle me-1"></i>{t.btnSelect}
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            <div className="d-flex justify-content-between align-items-center mt-4 pt-3 border-top">
                <button
                    className="btn btn-sm btn-outline-secondary rounded-pill px-3 shadow-sm"
                    onClick={handlePrevious}
                    disabled={currentPage === 0}
                >
                    <i className="bi bi-chevron-left me-1"></i>{t.btnPrevious}
                </button>

                <span className="text-muted small fw-medium">
                    {t.countShowing.replace('{0}', firstRow.toString())
                        .replace('{1}', Math.min(firstRow + pageData.page.size - 1, totalRows).toString())
                        .replace('{2}', totalRows.toString())}
                </span>

                <button
                    className="btn btn-sm btn-outline-secondary rounded-pill px-3 shadow-sm"
                    onClick={handleNext}
                    disabled={currentPage >= pageData.page.totalPages - 1}
                >
                    {t.btnNext}<i className="bi bi-chevron-right ms-1"></i>
                </button>
            </div>
        </div>
    );
};

(window as any).ReadOp = ReadOp;
