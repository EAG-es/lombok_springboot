const React = (window as any).React;
const { useState } = React;


interface FormData {
    id: string;
    order_number: string;
    product_id: string;
    quantity: string;
    total_price: string;
}

interface Translation {
    opsTitle: string;
    labelId: string;
    labelOrderNumber: string;
    labelProductId: string;
    labelQuantity: string;
    labelTotalPrice: string;
    placeholderId: string;
    placeholderOrderNumber: string;
    placeholderProductId: string;
    placeholderQuantity: string;
    placeholderTotalPrice: string;
    labelLoading: string;
    btnCreate: string;
    btnRead: string;
    btnUpdate: string;
    btnDelete: string;
    msgSuccess: string;
    msgPrompt: string;
}

// Global interface definitions for window properties
interface CustomWindow extends Window {
    i18n: { [key: string]: Translation };
    OperationPanel: React.FC;
}

const getW = (): CustomWindow => (window as any);

const OperationPanel: React.FC = () => {
    const [formData, setFormData] = useState({ id: '', order_number: '', product_id: '', quantity: '', total_price: '' } as FormData);
    const [status, setStatus] = useState(null as string | null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null as string | null);
    const lang = document.documentElement.lang || 'en';
    const t = getW().i18n[lang] || getW().i18n.en;

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prev: FormData) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (action: string) => {
        setLoading(true);
        setStatus(null);
        setError(null);

        const csrfToken = document.querySelector<HTMLMetaElement>('meta[name="_csrf"]')?.content;
        const csrfHeader = document.querySelector<HTMLMetaElement>('meta[name="_csrf_header"]')?.content;

        const payload = {
            location: window.location.href,
            service: document.body.className.replace('theme-', ''),
            action: action,
            data: formData
        };

        // Simulate async operation
        setTimeout(() => {
            console.log("Submitting to future endpoint:", payload);

            // Simulation of CSRF header usage
            if (csrfHeader && csrfToken) {
                console.log(`Prepared with CSRF: ${csrfHeader}=${csrfToken}`);
            }

            // Simulate success or error
            const isSuccess = action !== 'delete' || Math.random() > 0.5; // Delete has 50% error chance

            setLoading(false);
            if (isSuccess) {
                setStatus(t.msgSuccess.replace('{0}', action.toUpperCase()));
                setError(null);
            } else {
                setError(`Error occurred during ${action} operation.`);
                setStatus(null);
            }
        }, 2000); // 2 seconds delay
    };

    return (
        <div className="card shadow-lg border-0 mb-5">
            <div className="card-header bg-dark text-white px-4 py-3">
                <h4 className="mb-0 text-center">{t.opsTitle}</h4>
            </div>
            <div className="card-body p-4">
                <form className="mb-4" onSubmit={(e) => e.preventDefault()}>
                    <div className="row g-3">
                        <div className="col-md-2">
                            <label className="form-label fw-bold">{t.labelId}</label>
                            <input
                                type="text"
                                name="id"
                                className="form-control"
                                placeholder={t.placeholderId}
                                value={formData.id}
                                onChange={handleInputChange}
                            />
                        </div>
                        <div className="col-md-3">
                            <label className="form-label fw-bold">{t.labelOrderNumber}</label>
                            <input
                                type="text"
                                name="order_number"
                                className="form-control"
                                placeholder={t.placeholderOrderNumber}
                                value={formData.order_number}
                                onChange={handleInputChange}
                            />
                        </div>
                        <div className="col-md-2">
                            <label className="form-label fw-bold">{t.labelProductId}</label>
                            <input
                                type="text"
                                name="product_id"
                                className="form-control"
                                placeholder={t.placeholderProductId}
                                value={formData.product_id}
                                onChange={handleInputChange}
                            />
                        </div>
                        <div className="col-md-2">
                            <label className="form-label fw-bold">{t.labelQuantity}</label>
                            <input
                                type="text"
                                name="quantity"
                                className="form-control"
                                placeholder={t.placeholderQuantity}
                                value={formData.quantity}
                                onChange={handleInputChange}
                            />
                        </div>
                        <div className="col-md-3">
                            <label className="form-label fw-bold">{t.labelTotalPrice}</label>
                            <input
                                type="text"
                                name="total_price"
                                className="form-control"
                                placeholder={t.placeholderTotalPrice}
                                value={formData.total_price}
                                onChange={handleInputChange}
                            />
                        </div>
                    </div>

                    <div className="d-flex gap-3 mt-4 justify-content-center flex-wrap">
                        <button type="button" className="btn btn-lg btn-primary px-4" onClick={() => handleSubmit('create')} disabled={loading}>
                            <i className="bi bi-plus-circle me-2"></i>{t.btnCreate}
                        </button>
                        <button type="button" className="btn btn-lg btn-success px-4" onClick={() => handleSubmit('read')} disabled={loading}>
                            <i className="bi bi-search me-2"></i>{t.btnRead}
                        </button>
                        <button type="button" className="btn btn-lg btn-warning px-4" onClick={() => handleSubmit('update')} disabled={loading}>
                            <i className="bi bi-pencil-square me-2"></i>{t.btnUpdate}
                        </button>
                        <button type="button" className="btn btn-lg btn-danger px-4" onClick={() => handleSubmit('delete')} disabled={loading}>
                            <i className="bi bi-trash me-2"></i>{t.btnDelete}
                        </button>
                    </div>
                </form>

                {loading && (
                    <div className="alert alert-info mt-3 text-center border-0 shadow-sm animate__animated animate__fadeIn">
                        <strong><i className="bi bi-hourglass-split me-2"></i>{t.labelLoading}</strong>
                    </div>
                )}

                {error && (
                    <div className="alert alert-danger mt-3 text-center border-0 shadow-sm animate__animated animate__fadeIn">
                        <strong><i className="bi bi-exclamation-triangle me-2"></i>{error}</strong>
                    </div>
                )}

                {status && (
                    <div className="alert alert-success mt-3 text-center border-0 shadow-sm animate__animated animate__fadeIn">
                        <strong><i className="bi bi-check2-circle me-2"></i>{status}</strong>
                    </div>
                )}

                {!status && !loading && !error && (
                    <div className="text-center text-muted small italic">
                        {t.msgPrompt}
                    </div>
                )}
            </div>
        </div>
    );
};

getW().OperationPanel = OperationPanel;
