interface InventoryDTO {
    id: string;
    productId: string;
    quantity: string;
}

interface PageContent {
    content: InventoryDTO[];
    page: {
        totalElements: number;
        totalPages: number;
        number: number;
        size: number;
    };
}

interface ReadOpProps {
    location: string;
    inventoryDTO: InventoryDTO;
    setInventoryDTO: (data: InventoryDTO) => void;
    onSuccess: (selectedInventory: InventoryDTO, searchCriteria: InventoryDTO, page: number) => void;
    onError: (error: string) => void;
    onBack: (searchCriteria: InventoryDTO, page: number) => void;
    lang: string;
    refreshKey?: number;
    initialPage?: number;
}

const ReadOp: React.FC<ReadOpProps> = ({ location, inventoryDTO, setInventoryDTO, onSuccess, onError, onBack, lang, refreshKey, initialPage = 0 }) => {
    const [loading, setLoading] = (window as any).React.useState(true);
    const [pageData, setPageData] = (window as any).React.useState(null as PageContent | null);
    const [currentPage, setCurrentPage] = (window as any).React.useState(initialPage);
    const [searchCriteria, setSearchCriteria] = (window as any).React.useState(inventoryDTO);
    const t = (window as any).i18n[lang] || (window as any).i18n.en;

    const csrfToken = document.querySelector<HTMLMetaElement>('meta[name="_csrf"]')?.content;
    const csrfHeader = document.querySelector<HTMLMetaElement>('meta[name="_csrf_header"]')?.content;

    const fetchPage = async (page: number, criteria?: InventoryDTO) => {
        setLoading(true);
        const searchData = criteria || searchCriteria;
        try {
            const url = new URL('/inventory/select', window.location.origin);
            url.searchParams.append('page', page.toString());
            url.searchParams.append('pageSize', '10');

            const response = await fetch(url.toString(), {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Location': location,
                    [csrfHeader || 'X-CSRF-TOKEN']: csrfToken || ''
                },
                body: JSON.stringify(searchData)
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({ message: `HTTP ${response.status}: ${response.statusText}` }));
                throw new Error(errorData.message || `HTTP ${response.status}: ${response.statusText}`);
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

    (window as any).React.useEffect(() => {
        fetchPage(initialPage, inventoryDTO);
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

    const handleRowClick = (inventory: InventoryDTO) => {
        const selectedInventory = {
            id: inventory.id || '',
            productId: inventory.productId || '',
            quantity: inventory.quantity || ''
        };
        setInventoryDTO(selectedInventory);
        onSuccess(selectedInventory, searchCriteria, currentPage);
    };

    const handleBack = () => {
        onBack(searchCriteria, currentPage);
    };

    const firstRow = pageData ? currentPage * pageData.page.size + 1 : 0;
    const totalRows = pageData ? pageData.page.totalElements : 0;

    if (loading) {
        return (
            <div className="p-4 border border-success rounded bg-light shadow-sm">
                <div className="text-center">
                    <div className="spinner-border text-success" role="status">
                        <span className="visually-hidden">Loading...</span>
                    </div>
                    <p className="mt-2">{t.labelLoading}</p>
                </div>
            </div>
        );
    }

    if (!pageData || pageData.content.length === 0) {
        return (
            <div className="p-4 border border-success rounded bg-light shadow-sm">
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <h4 className="text-success mb-0">{t.labelViewItems}</h4>
                    <button className="btn btn-outline-secondary btn-sm" onClick={handleBack}>
                        &larr; {t.btnBack}
                    </button>
                </div>
                <div className="alert alert-info">{t.labelNoRecords}</div>
            </div>
        );
    }

    return (
        <div className="p-4 border border-success rounded bg-light shadow-sm">
            <div className="d-flex justify-content-between align-items-center mb-3">
                <h4 className="text-success mb-0">{t.labelViewItems}</h4>
                <button className="btn btn-outline-secondary btn-sm" onClick={handleBack}>
                    &larr; {t.btnBack}
                </button>
            </div>
            <table className="table table-hover">
                <thead className="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>{t.labelProductId}</th>
                        <th>{t.labelQuantity}</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {pageData.content.map((inventory: InventoryDTO) => (
                        <tr key={inventory.id}>
                            <td>{inventory.id}</td>
                            <td>{inventory.productId}</td>
                            <td>{inventory.quantity}</td>
                            <td>
                                <button
                                    className="btn btn-sm btn-circle btn-outline-primary"
                                    onClick={() => handleRowClick(inventory)}
                                    title="Select"
                                >
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                                        <circle cx="8" cy="8" r="6" fill="none" stroke="currentColor" strokeWidth="1.5" />
                                    </svg>
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <div className="d-flex justify-content-between align-items-center mt-3">
                <button
                    className="btn btn-outline-success"
                    onClick={handlePrevious}
                    disabled={currentPage === 0}
                >
                    &larr; {t.btnPrevious}
                </button>
                <span className="text-muted">
                    {firstRow} - {Math.min(firstRow + pageData.page.size - 1, totalRows)} / {totalRows}
                </span>
                <button
                    className="btn btn-outline-success"
                    onClick={handleNext}
                    disabled={currentPage >= pageData.page.totalPages - 1}
                >
                    {t.btnNext} &rarr;
                </button>
            </div>
        </div>
    );
};

(window as any).ReadOp = ReadOp;
