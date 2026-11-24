import {
  HttpClient,
  HttpContext,
  HttpErrorResponse,
  provideHttpClient,
  withInterceptors,
} from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { MatSnackBar } from '@angular/material/snack-bar';
import type { Mock } from 'vitest';
import { errorInterceptor, HANDLED_ERROR_STATUSES } from './error-interceptor';

describe('errorInterceptor', () => {
  let http: HttpClient;
  let httpTesting: HttpTestingController;
  let snackBar: { open: Mock };

  beforeEach(() => {
    snackBar = { open: vi.fn() };
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([errorInterceptor])),
        provideHttpClientTesting(),
        { provide: MatSnackBar, useValue: snackBar },
      ],
    });
    http = TestBed.inject(HttpClient);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTesting.verify();
  });

  it('should show the detail of a problem', () => {
    http.get('/api/v1/accounts/42').subscribe({ error: () => {} });

    httpTesting
      .expectOne('/api/v1/accounts/42')
      .flush(
        { status: 404, title: 'Not Found', detail: 'Account not found.' },
        { status: 404, statusText: 'Not Found' },
      );

    expect(snackBar.open).toHaveBeenCalledWith('Account not found.', 'Close', { duration: 5000 });
  });

  it('should let the caller receive the error', () => {
    let received = false;
    http.get('/api/v1/accounts').subscribe({ error: () => (received = true) });

    httpTesting
      .expectOne('/api/v1/accounts')
      .flush(null, { status: 500, statusText: 'Server Error' });

    expect(received).toBe(true);
  });

  it('should not show the snackbar for a status handled by the caller', () => {
    let error: HttpErrorResponse | undefined;
    http
      .get('/api/v1/accounts', { context: new HttpContext().set(HANDLED_ERROR_STATUSES, [404]) })
      .subscribe({ error: (err) => (error = err) });

    httpTesting.expectOne('/api/v1/accounts').flush(null, { status: 404, statusText: 'Not Found' });

    expect(snackBar.open).not.toHaveBeenCalled();
    expect(error?.status).toBe(404);
  });

  it('should still show the snackbar for a status not handled by the caller', () => {
    http
      .get('/api/v1/accounts', { context: new HttpContext().set(HANDLED_ERROR_STATUSES, [404]) })
      .subscribe({ error: () => {} });

    httpTesting
      .expectOne('/api/v1/accounts')
      .flush(null, { status: 500, statusText: 'Server Error' });

    expect(snackBar.open).toHaveBeenCalled();
  });
});
